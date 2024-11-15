import Speech from "speak-tts";

export default {
  speechInit() {
    this.speech = new Speech();
    this.speech.init({
      volume: 1,
      // en-GB、zh-CN
      lang: "en-GB",
      rate: 1,
      pitch: 1,
      voice: 'Microsoft Hazel - English (United Kingdom)',
      listeners: {
        'onvoiceschanged': (voices) => {
          console.log("Event voiceschanged", voices)
        }
      }
    }).then(() => {
    })
  },
  //语音播报
  speak(content) {
    this.speech.speak({text: content, queue: false,}).then(() => {
    })
  },
  changeVoice(language) {
    if (language === 'en-US') {
      this.speech.setVoice('Microsoft Zira - English (United States)')
    } else if (language === 'en-GB') {
      this.speech.setVoice('Microsoft Hazel - English (United Kingdom)')
    }
    this.speech.setLanguage(language)
  },
  pauseSpeak() {
    this.speech.pause()
  },
  resumeSpeak() {
    this.speech.resume()
  }
}
