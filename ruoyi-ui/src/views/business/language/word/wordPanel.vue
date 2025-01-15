<template>
  <div>
    <el-row :gutter="20" justify="center" type="flex">
      <el-col :span="6">
        <el-button v-if="!this.review" :disabled="backDisable" type="primary" @click="forward()">上一个</el-button>
      </el-col>
      <el-col :span="6">
        <div v-if="!this.review">
          <el-statistic
            :value="this.wordIndex+1"
            :precision="0"
            group-separator=","
            title="当前"
          ></el-statistic>
        </div>
      </el-col>
      <el-col :span="6">
        <div v-if="!this.review">
          <el-statistic :precision="0"
                        :value="totalNum"
                        group-separator=","
                        title="总数">
          </el-statistic>
        </div>
      </el-col>
      <el-col :span="6">
        <el-button v-if="!this.review" :disabled="nextDisable" type="primary" @click="nextWord()">下一个</el-button>
      </el-col>
    </el-row>
    <el-descriptions v-show="showWordInfo" :column="2" border direction="vertical" title="单词信息">
      <el-descriptions-item label="单词">
        <div style=" width: 200px ">
          <span class="ok-content">{{ okTxt }}</span>
          <span class="error-content">{{ notInputtedTxt }}</span>
        </div>
      </el-descriptions-item>
      <el-descriptions-item label="释义">
        <div style=" width: 500px; ">
          {{ oneWord.translation }}
        </div>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-show="showWordInfo" :column="2" border class="margin-top" title="发音">
      <el-descriptions-item label="美式发音">
        <template slot="label">
          美式发音
        </template>
        <el-button
          icon="el-icon-microphone"
          size="mini"
          type="text"
          @click="playAudio(US,oneWord.word)"
        >播放
        </el-button>
      </el-descriptions-item>
      <el-descriptions-item label="英式发音">
        <template slot="label">
          英式发音
        </template>
        <el-button
          icon="el-icon-microphone"
          size="mini"
          type="text"
          @click="playAudio(UK,oneWord.word)"
        >播放
        </el-button>
      </el-descriptions-item>
    </el-descriptions>
    <el-collapse v-show="showWordInfo && sentenceList.length > 0" accordion>
      <div class="margin-top" style="font-size: 16px;font-weight: bold;">例句</div>
      <el-collapse-item v-for="(sentence,index) in sentenceList" :key="sentence.uuid"
                        :title="sentence.sentenceContent">
        <div>{{ sentence.translateContent }}
          <el-button
            icon="el-icon-microphone"
            size="mini"
            type="text"
            @click="playOneWordAudio(sentence.sentenceContent)"
          >
          </el-button>
        </div>
      </el-collapse-item>
    </el-collapse>
    <el-descriptions v-show="showWordInfo" :colon="false" class="margin-top" title="内容填写区">
      <el-descriptions-item>
        <el-input
          v-model="currentInputTxt"
          placeholder="请输入内容"
          resize="none"
          rows="2" type="textarea"
          @input="userInputs"/>
      </el-descriptions-item>
    </el-descriptions>
    <el-empty v-show="!showWordInfo" description="没有啦"></el-empty>
  </div>
</template>

<script>
import {getOneWord, getOneWordInCollection} from "@/api/bussiness/word";
import {addUserWord} from "@/api/bussiness/userword";
import {addRecord} from "@/api/bussiness/record";
import {getCollectionTotalAndNotStudyNum, getTotalAndNotStudyNum} from "@/api/statistics/statistics";
import {getReviewWord} from "@/api/review/review";

export default {
  name: "WordPanel",
  props: {
    wordCollectionId: {
      type: String,
      default: ""
    },
    review: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      title: '',
      oneWord: {
        word: "",
        uuid: "",
        phonetic: "",
        translation: "",
        pos: "",
        labelList: [],
      },
      currentInputTxt: '',
      oneWordTxt: '',
      okTxt: '',
      notInputtedTxt: '',
      enterCount: 0,
      // 学习记录
      record: {
        word: '',
        wordUuid: ''
      },
      totalNum: 0,
      showWordInfo: true,
      wordIndex: 0,
      currentLexiconUUID: "",
      backDisable: false,
      nextDisable: false,
      sentenceList: []
    }
  },
  async mounted() {
    this.clearPanelData()
    // 更新统计数据
    if (this.wordCollectionId) {
      let params = {
        wordCollectionId: this.wordCollectionId
      }
      await getCollectionTotalAndNotStudyNum(params).then(res => {
        this.totalNum = res.data.total;
        this.starStudy()
      });
    } else {
      await getTotalAndNotStudyNum().then(res => {
        this.totalNum = res.data.total;
        this.starStudy(this.review)
      });
    }
  },
  methods: {
    async starStudy(isReview) {
      await this.getOneWord(this.wordIndex, isReview);
    },
    async forward() {
      this.clearPanelData()
      let currentIndex = --this.wordIndex;
      await this.getOneWord(currentIndex, this.review);
    },
    async nextWord() {
      this.clearPanelData()
      let currentIndex = ++this.wordIndex;
      await this.getOneWord(currentIndex, this.review);
    },
    setBtnStatus: function (index) {
      this.backDisable = false
      this.nextDisable = false
      // 边界值判断
      if (this.totalNum === 1) {
        this.nextDisable = true
        this.backDisable = true
        return
      }
      if (index + 1 >= this.totalNum) {
        this.nextDisable = true
        return
      }
      if (index <= 0) {
        this.backDisable = true
      }
    }, processResData: function (response, index) {
      if (!response.data) {
        this.showWordInfo = false;
        return;
      }
      this.setBtnStatus(index);
      this.oneWord = response.data;
      this.oneWordTxt = this.oneWord.word;
      this.notInputtedTxt = this.oneWordTxt;
      this.sentenceList = []
      if (response.data.sentenceList && response.data.sentenceList.length > 0) {
        this.sentenceList = [...response.data.sentenceList]
      }
      if (this.showWordInfo) {
        this.okTxt = '';
        this.playOneWordAudio(this.oneWordTxt)
      }
    },
    async getOneWord(index, isReview) {
      if (this.totalNum === 0) {
        return
      }
      if (index < 0) {
        this.wordIndex = 0
        index = 0
      }
      this.clearPanelData();
      if (this.wordCollectionId) {
        await getOneWordInCollection(this.wordCollectionId, index).then(response => {
          this.processResData(response, index);
        });
      } else {
        if (isReview) {
          await getReviewWord().then(res => {
            this.processResData(res, index)
          });
          return
        }
        await getOneWord(this.currentLexiconUUID, index).then(response => {
          this.processResData(res, index)
        });
      }
    },
    // 播放音频
    playAudio(language, word) {
      this.speakCommon.changeVoice(language)
      this.speakCommon.speak(word)
    },
    playOneWordAudio(word) {
      this.speakCommon.speak(word)
      // this.audio.src = "http://dict.youdao.com/dictvoice?type=0&audio=" + this.oneWordTxt;
      // this.audio.play();
    },
    clearPanelData: function () {
      this.enterCount = 0;
      this.currentInputTxt = '';
      this.notInputtedTxt = '';
    },
    async userInputs() {
      console.log("====================" + this.currentInputTxt)
      if ((this.currentInputTxt.length === this.oneWordTxt.length) &&
        (this.currentInputTxt[this.currentInputTxt.length - 1] === this.oneWordTxt[this.currentInputTxt.length - 1])) {
        this.clearPanelData();
        this.okTxt = this.oneWordTxt;
        this.record.wordUuid = this.oneWord.uuid;
        this.record.word = this.oneWord.word;
        // 我的单词+1
        await addUserWord(this.record).then(() => {
        });
        // 新增学习记录
        await addRecord(this.record).then(() => {
        });
        this.wordIndex++;
        if (this.wordIndex >= this.totalNum) {
          this.wordIndex = this.wordIndex - 1
          return;
        }
        await this.getOneWord(this.wordIndex, this.review);
        if (this.review) {
          this.$emit("handleNextWord")
        }
        return;
      }
      for (let i = 0; i < this.currentInputTxt.length; i++) {
        if (this.currentInputTxt[i] === this.oneWordTxt[i]) {
          this.enterCount = i + 1;
          this.okTxt = this.oneWordTxt.slice(0, this.enterCount);
          this.notInputtedTxt = this.oneWordTxt.slice(this.enterCount, this.oneWordTxt.length);
        } else {
          this.currentInputTxt = this.okTxt;
          break;
        }
      }
    }
  }
}
</script>
<style>
.margin-top {
  margin-top: 20px;
}

.ok-content {
  color: #42b983;
  font-size: large;
}

.error-content {
  color: #E65D6E;
  font-size: large;
}
</style>
