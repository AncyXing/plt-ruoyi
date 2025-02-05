package com.example.pltool.controller.business.flashcard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pltool.domain.dto.flashcard.card.AddCardDto;
import com.example.pltool.domain.dto.flashcard.card.BatchAddCardDto;
import com.example.pltool.domain.dto.flashcard.card.CancelAssociationDto;
import com.example.pltool.domain.dto.flashcard.card.PackageCardInfo;
import com.example.pltool.domain.entity.Flashcard;
import com.example.pltool.service.flashcard.FlashcardService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * <p>
 * 闪卡表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/flashcard")
public class FlashcardController extends BaseController {
  @Autowired
  private FlashcardService flashcardService;

  /**
   * 查询卡片列表
   */
  @GetMapping("/list")
  public TableDataInfo list() {
    startPage();
    QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("create_time");
    List<Flashcard> list = flashcardService.list(queryWrapper);
    return getDataTable(list);
  }

  /**
   * 新增闪卡
   */
  @PreAuthorize("@ss.hasPermi('system:flashcard:add')")
  @Log(title = "闪卡", businessType = BusinessType.INSERT)
  @PostMapping("/add")
  public AjaxResult add(@RequestBody AddCardDto addCardDto) {
    Long userId = getUserId();
    return AjaxResult.success(flashcardService.addCard(addCardDto, userId));
  }

  @GetMapping("/getCardInfo/{uuid}")
  public AjaxResult getCardInfo(@PathVariable("uuid") String uuid) {
    return AjaxResult.success(flashcardService.getCardByUUId(uuid));
  }

  @PostMapping("/update")
  public AjaxResult update(@RequestBody Flashcard flashcard) {
    return AjaxResult.success(flashcardService.update(flashcard, getUserId()));
  }

  @DeleteMapping("/delete/{uuid}")
  public AjaxResult delete(@PathVariable("uuid") String uuid) {
    return AjaxResult.success(flashcardService.delete(uuid));
  }

  /**
   * 获取卡包中的一张闪卡
   */
  @GetMapping("/getCardOfPackage")
  public AjaxResult getCardOfPackage(@RequestParam("packageUUID") String packageUUID,
      @RequestParam("offset") Integer offset) {
    return AjaxResult.success(flashcardService.getCardOfPackage(packageUUID, offset));
  }

  /**
   * 获取卡包中的所有卡片
   */
  @GetMapping("/getCardListInPackage")
  public AjaxResult getCardListInPackage(@RequestParam("packageUUID") String packageUUID) {
    return AjaxResult.success(flashcardService.getCardListInPackage(packageUUID));
  }

  @GetMapping("/searchClassifyCard")
  public AjaxResult searchClassifyCard(@RequestParam("packageUUID") String packageUUID,
      @RequestParam("offset") Integer offset, @RequestParam("type") Integer type) {
    return AjaxResult.success(flashcardService.searchClassifyCard(packageUUID, type, offset));
  }

  /**
   * 卡片关联卡包
   */
  @PostMapping("/addCardsToPackage")
  public AjaxResult addCardsToPackage(@RequestBody PackageCardInfo packageCardInfo) {
    packageCardInfo.setUserId(getUserId());
    return AjaxResult.success(flashcardService.addCardsToPackage(packageCardInfo));
  }

  /**
   * 批量新增卡片
   */
  @PostMapping("/batchAddCard")
  public AjaxResult batchAddCard(@RequestBody BatchAddCardDto batchAddCardDto) {
    batchAddCardDto.setUserId(getUserId());
    return flashcardService.batchAddCard(batchAddCardDto);
  }

  /**
   * 查询卡片关联的卡包信息
   */
  @GetMapping("/getPackageInfoOfCard")
  public TableDataInfo getPackageInfoOfCard(@RequestParam("cardUUId") String cardUUId) {
    startPage();
    return getDataTable(flashcardService.getPackageInfoOfCard(cardUUId));
  }

  /**
   * 卡片关联卡包
   */
  @PostMapping("/cancelAssociation2Package")
  public AjaxResult cancelAssociation2Package(
      @RequestBody CancelAssociationDto cancelAssociationDto) {
    cancelAssociationDto.setUserId(getUserId());
    return flashcardService.cancelAssociation2Package(cancelAssociationDto);
  }
}
