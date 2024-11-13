package com.example.pltool.service.impl.flashcard;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.controller.business.constant.enums.CardTypeEnum;
import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.flashcard.cardpackage.OperateWordInCollection;
import com.example.pltool.domain.entity.Flashcard;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.PackageCardRef;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.mapper.FlashcardMapper;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.flashcard.CreateCardService;
import com.example.pltool.service.flashcard.FlashcardService;
import com.ruoyi.common.core.domain.AjaxResult;

@Service
public class WordCardCreator extends ServiceImpl<FlashcardMapper, Flashcard>
        implements CreateCardService<Word> {
    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private PackageCardRefService packageCardRefService;

    @Autowired
    private FlashcardService flashcardService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult createCardAndRelationShip(OperateWordInCollection operateWordInCollection,
                                                List<Word> sourceItem) {
        if (CollectionUtils.isEmpty(sourceItem)) {
            return AjaxResult.success(true);
        }
        List<Flashcard> flashcards = new ArrayList<>();
        List<PackageCardRef> packageCardRefs = new ArrayList<>();
        List<String> sourceUUIdList =
                sourceItem.stream().map(Word::getUuid).collect(Collectors.toList());
        QueryWrapper<Flashcard> flashcardQueryWrapper = new QueryWrapper<>();
        flashcardQueryWrapper.in("source_uuid", sourceUUIdList).eq("user_id",
                operateWordInCollection.getUserId());
        List<Flashcard> existCardList = flashcardService.list(flashcardQueryWrapper);
        Map<String, Flashcard> existFlashcardMap =
                existCardList.stream().collect(Collectors.toMap(Flashcard::getUuid, Function.identity()));
        List<String> existWordCardSourceUUIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(existCardList)) {
            existWordCardSourceUUIdList.addAll(existCardList.stream().map(Flashcard::getSourceUuid)
                    .filter(Objects::nonNull).collect(Collectors.toList()));
        }
        sourceItem.stream().filter(Objects::nonNull).forEach(word -> {
            if (CollectionUtils.isEmpty(existWordCardSourceUUIdList)
                    || !existWordCardSourceUUIdList.contains(word.getUuid())) {
                Flashcard flashcard = new Flashcard();
                flashcard.setUserId(operateWordInCollection.getUserId());
                String flashcardUUUID = UUID.randomUUID().toString().replace("-", "");
                flashcard.setUuid(flashcardUUUID);
                flashcard.setFront(word.getWord());
                flashcard.setBack(word.getTranslation());
                flashcard.setType(CardTypeEnum.WORD.getValue());
                flashcard.setSourceUuid(word.getUuid());
                flashcards.add(flashcard);
                // 卡片与卡包的关联关系
                List<PackageCardRef> buildPackageCardRefList =
                        buildPackageCardRefList(operateWordInCollection, flashcardUUUID);
                if (!CollectionUtils.isEmpty(buildPackageCardRefList)) {
                    packageCardRefs.addAll(buildPackageCardRefList);
                }
            }
        });
        existFlashcardMap.forEach((k, v) -> {
            List<PackageCardRef> buildPackageCardRefList =
                    buildPackageCardRefList(operateWordInCollection, k);
            if (!CollectionUtils.isEmpty(buildPackageCardRefList)) {
                packageCardRefs.addAll(buildPackageCardRefList);
            }
        });

        if (!CollectionUtils.isEmpty(flashcards)) {
            saveBatch(flashcards);
        }
        if (!CollectionUtils.isEmpty(packageCardRefs)) {
            packageCardRefService.saveBatch(packageCardRefs);
        }
        return AjaxResult.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult delCardRelationShip(List<String> UUIdList, String collectionUUId, Long userId) {
        // 单词和单词集
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_type", RefTypeEnum.WORD.getValue());
        queryWrapper.eq("label_uuid", collectionUUId);
        queryWrapper.in("ref_uuid", UUIdList);
        queryWrapper.eq("create_user_id", userId);
        labelRefService.remove(queryWrapper);
        QueryWrapper<Flashcard> flashcardQueryWrapper = new QueryWrapper<>();
        flashcardQueryWrapper.in("source_uuid", UUIdList);
        flashcardQueryWrapper.eq("user_id", userId);
        List<Flashcard> flashCardList = flashcardService.list(flashcardQueryWrapper);
        if (!CollectionUtils.isEmpty(flashCardList)) {
            List<String> cardUUIdList =
                    flashCardList.stream().map(Flashcard::getUuid).collect(Collectors.toList());
            // 删除卡片关联关系
            QueryWrapper<PackageCardRef> cardRefQueryWrapper = new QueryWrapper<>();
            cardRefQueryWrapper.eq("collection_uuid", collectionUUId);
            cardRefQueryWrapper.eq("create_user_id", userId);
            cardRefQueryWrapper.in("card_uuid", cardUUIdList);
            packageCardRefService.remove(cardRefQueryWrapper);
        }
        return AjaxResult.success(true);
    }

    private List<PackageCardRef> buildPackageCardRefList(
            OperateWordInCollection operateWordInCollection, String flashcardUUUID) {
        List<PackageCardRef> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(operateWordInCollection.getPackageUUIdList())) {
            operateWordInCollection.getPackageUUIdList().forEach(e -> {
                PackageCardRef packageCardRef = new PackageCardRef();
                packageCardRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
                packageCardRef.setCardUuid(flashcardUUUID);
                packageCardRef.setPackageUuid(e);
                packageCardRef.setCollectionUuid(operateWordInCollection.getCollectionUUID());
                packageCardRef.setCreateUserId(operateWordInCollection.getUserId());
                list.add(packageCardRef);
            });
        }
        return list;
    }
}
