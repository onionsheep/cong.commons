var mixedMode = {
  name: "htmlmixed",
  scriptTypes: [{matches: /\/x-handlebars-template|\/x-mustache/i,
    mode: null},
    {matches: /(text|application)\/(x-)?vb(a|script)/i,
      mode: "vbscript"}]
};
var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
  lineNumbers: true,
  mode: mixedMode
});