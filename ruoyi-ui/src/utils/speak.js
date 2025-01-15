import Speech from "speak-tts";
import store from '@/store'
export default {
  speechInit() {
    this.speech = new Speech();
    this.speech.init({
      'rate': 1,
      'pitch': 1,
      'splitSentences': true,
    }).then((data) => {
      // 存储不同发音列表
      var USVoices=[];
      var GBVoices=[];
      var voices = data.voices;
      for(var i = 0 ; i < voices.length; i++) {
        if(voices[i].lang === 'en-US' && voices[i].localService) {
          USVoices.push(voices[i]);
        }
        if(voices[i].lang === 'en-GB' && voices[i].localService) {
          GBVoices.push(voices[i]);
        }
      }
      store.dispatch('language/changeVoiceNameSetting', {
        key: 'USVoices',
        value: USVoices
      })
      store.dispatch('language/changeVoiceNameSetting', {
        key: 'GBVoices',
        value: GBVoices
      })
      const storageSetting = JSON.parse(localStorage.getItem('language-setting')) || ''
      if(storageSetting){
        this.speech.setLanguage('en-GB')
        this.speech.setVoice(store.state.language.GBName)
        return
      }
      // 设定默认值，为英音
      let defaultVoice = GBVoices.pop();
      let defaultUsVoice = USVoices.pop();
      console.log(defaultUsVoice, '=======', defaultVoice)
      store.dispatch('language/changeVoiceNameSetting', {
        key: 'GBName',
        value: defaultVoice.name
      })
      store.dispatch('language/changeVoiceNameSetting', {
        key: 'USName',
        value: defaultUsVoice.name
      })
      this.speech.setLanguage(defaultVoice.lang)
      this.speech.setVoice(defaultVoice.name)
    })
  },
  //语音播报
  speak(content) {
    this.speech.speak({text: content, queue: false,}).then(() => {
    })
  },
  changeVoice(language) {
    if (language === 'en-US') {
      this.speech.setVoice(store.state.language.USName)
    } else if (language === 'en-GB') {
      this.speech.setVoice(store.state.language.GBName)
    }
    this.speech.setLanguage(language)
  },
  updateVoice() {
      this.speech.setVoice(store.state.language.GBName)
  },
  pauseSpeak() {
    this.speech.pause()
  },
  resumeSpeak() {
    this.speech.resume()
  }
}
