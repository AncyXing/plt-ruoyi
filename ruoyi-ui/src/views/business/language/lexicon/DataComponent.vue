<script>
import {addLexicon, delLexicon, getLexicon, updateLexicon} from "@/api/bussiness/lexicon";

export default {
  props: {
    // lexiconData : Object,
    lexiconId: Number
  },
  data() {
    return {
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
        language: [
          {required: true, message: "语言不能为空", trigger: "blur"}
        ]
      },
      fileList: [],
      // 动态添加的标签
      dynamicTags: [],
      editTags: [],
      deleteTags: [],
      inputVisible: false,
      inputValue: '',
      isInsert: true
    }
  },
  watch: {
    lexiconId: {
      handler(val) {
        this.dynamicTags = []
        this.deleteTags = []
        if (val !== this.form.id) {
          if (val === null) {
            this.form = {
              file: []
            }
            this.isInsert = true
          } else {
            this.isInsert = false
            getLexicon(val).then(response => {
              this.form = response.data;
              console.log(this.form)
              if (response.data.labelList != null) {
                this.dynamicTags = [...response.data.labelList]
              }
            });
          }
        }
      },
      immediate: true,
    },
  },

  created() {
    // props 会暴露到 `this` 上
    if (this.lexiconId !== null) {
      getLexicon(this.lexiconId).then(response => {
        this.form = response.data;
        if (response.data.labelList != null) {
          this.dynamicTags = []
          this.deleteTags = []
          if (response.data.labelList != null) {
            this.dynamicTags = [...response.data.labelList]
          }
        }
      });
    }
  },
  methods: {
    handleClose(tag, index) {
      const deletedTag = this.dynamicTags.splice(index, 1)[0];
      if (this.form.id != null) {
        this.deleteTags.push(deletedTag)
      }
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      this.inputVisible = false;
      let inputValue = this.inputValue;
      if (inputValue) {
        const newTag = {
          name: inputValue
        };
        this.dynamicTags.push(newTag);
      }
      this.inputValue = '';
    },

    showEditTagInput(index) {
      this.resetEditTagsFlag()
      this.$set(this.editTags, index, true)
      this.$nextTick(() => {
        var editInput = 'editInput' + index;
        this.$refs[editInput][0].$refs.input.focus()
      })
    },

    handleEditableInputConfirm(item, index) {
      if (item.name) {
        this.$set(this.editTags, index, false)
      } else {
        this.$message({message: "请填入标签", type: "info"})
      }
    },

    handleEditableInputBlur(item, index) {
      this.$set(this.editTags, index, false)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log(this.form)
          if (this.form.id != null) {
            let data = this.form;
            updateLexicon(data).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.$emit('closeDig')
            });
          } else {
            this.submitUpload();
            let labelNames = [];
            for (let i = 0; i < this.dynamicTags.length; i++) {
              labelNames[i] = this.dynamicTags[i].name
            }
            const formData = new FormData();
            formData.append("file", this.fileList[0])
            addLexicon(formData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.$emit('closeDig')
            });
          }
        }
      });
    },
    // 取消按钮
    cancel() {
      this.form = {};
      this.deleteTags = []
      this.$emit('closeDig')
    },
    uploadFile(param) {
      this.fileList.push(param.file);
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    resetEditTagsFlag() {
      for (let i = 0; i < this.editTags.length; i++) {
        this.editTags[i] = false
      }
    }
  }
}
</script>

<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="词库文件" v-show="isInsert">
        <el-upload
          class="upload-demo"
          ref="upload"
          action=""
          :http-request="uploadFile"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false">
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <div slot="tip" class="el-upload__tip">只能上传json文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
      <el-row :gutter="20" type="flex" justify="end">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </el-row>
    </el-form>
  </div>
</template>

<style>

.input-new-tag {
  //width: 70px;
  height: 32px;
  //margin-left: 10px;
  vertical-align: bottom;
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

</style>
