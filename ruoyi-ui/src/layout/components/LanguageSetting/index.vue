<script>
export default {
  name: "Language_Setting",
  components: {},
  data() {
    return {
      gbName: '',
      usName: '',
    }
  },
  computed: {
    visible: {
      get() {
        return this.$store.state.language.showSettings
      }
    },
    gbOptions: {
      get() {
        return this.$store.state.language.GBVoices
      }
    },
    usOptions: {
      get() {
        return this.$store.state.language.USVoices
      }
    }
  },
  methods: {
    saveSetting() {
      this.$modal.loading("正在保存到本地，请稍候...");
      this.$cache.local.set(
        "language-setting",
        `{
            "GBName": "${this.gbName}",
            "USName": "${this.usName}"
          }`
      );
      setTimeout(this.$modal.closeLoading(), 1000)
      this.closeSetting()
      this.$store.dispatch('language/changeVoiceNameSetting', {
        key: 'GBName',
        value: this.gbName
      })
      this.$store.dispatch('language/changeVoiceNameSetting', {
        key: 'USName',
        value: this.usName
      })
      this.speakCommon.updateVoice()
    },
    resetSetting() {
      this.$modal.loading("正在清除设置缓存并刷新，请稍候...");
      this.$cache.local.remove("language-setting")
      setTimeout("window.location.reload()", 1000)
    },
    closeSetting() {
      this.$store.dispatch('language/changeVoiceNameSetting', {
        key: 'showSettings',
        value: false
      })
    }
  },
  created() {
    this.gbName = this.$store.state.language.GBName;
    this.usName = this.$store.state.language.USName;
  }
}

</script>

<template>
  <el-drawer size="280px" :visible="visible"  :append-to-body="true" :show-close="true" :before-close="closeSetting">
    <div class="drawer-container">
      <div>
        <h3 class="drawer-title">语言发音设置</h3>
        <el-divider/>
        <div class="drawer-item">
          <span>英音 </span>
          <el-select v-model="gbName" placeholder="请选择">
            <el-option
              v-for="item in gbOptions"
              :key="item.name"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </div>

        <div class="drawer-item">
          <span>美音 </span>
          <el-select v-model="usName" placeholder="请选择">
            <el-option
              v-for="item in usOptions"
              :key="item.name"
              :label="item.name"
              :value="item.name">
            </el-option>
          </el-select>
        </div>

        <el-divider/>

        <el-button size="small" type="primary" plain icon="el-icon-document-add" @click="saveSetting">保存配置
        </el-button>
        <el-button size="small" plain icon="el-icon-refresh" @click="resetSetting">重置配置</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<style scoped lang="scss">
.drawer-container {
  padding: 20px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
  }

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
  }
}
</style>
