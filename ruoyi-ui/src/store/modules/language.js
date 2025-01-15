const storageSetting = JSON.parse(localStorage.getItem('language-setting')) || ''

const state = {
  USVoices: [],
  GBVoices: [],
  USName: storageSetting.USName,
  GBName: storageSetting.GBName,
  showSettings: false
}

const mutations = {
  CHANGE_SETTING: (state, {key, value}) => {
    if (state.hasOwnProperty(key)) {
      state[key] = value
    }
  }
}

const actions = {
  changeVoiceNameSetting({commit}, data) {
    commit('CHANGE_SETTING', data)
  }
}


export default {
  namespaced: true,
  state,
  mutations,
  actions
}
