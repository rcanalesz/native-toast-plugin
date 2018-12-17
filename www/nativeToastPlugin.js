// Empty constructor
function NativeToastPlugin() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
NativeToastPlugin.prototype.show = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'NativeToastPlugin', 'show', [options]);
}

ToastyPlugin.prototype.call = function( successCallback, errorCallback) {
  var options = {};
  cordova.exec(successCallback, errorCallback, 'NativeToastPlugin', 'call', [options]);
}

// Installation constructor that binds NativeToastPlugin to window
NativeToastPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.nativeToastPlugin = new NativeToastPlugin();
  return window.plugins.nativeToastPlugin;
};
cordova.addConstructor(NativeToastPlugin.install);