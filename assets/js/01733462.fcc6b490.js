"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[1170],{3905:(e,t,n)=>{n.d(t,{Zo:()=>s,kt:()=>k});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},o=Object.keys(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(r=0;r<o.length;r++)n=o[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var p=r.createContext({}),l=function(e){var t=r.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},s=function(e){var t=l(e.components);return r.createElement(p.Provider,{value:t},e.children)},f="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},u=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,o=e.originalType,p=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),f=l(n),u=a,k=f["".concat(p,".").concat(u)]||f[u]||m[u]||o;return n?r.createElement(k,i(i({ref:t},s),{},{components:n})):r.createElement(k,i({ref:t},s))}));function k(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=n.length,i=new Array(o);i[0]=u;var c={};for(var p in t)hasOwnProperty.call(t,p)&&(c[p]=t[p]);c.originalType=e,c[f]="string"==typeof e?e:a,i[1]=c;for(var l=2;l<o;l++)i[l]=n[l];return r.createElement.apply(null,i)}return r.createElement.apply(null,n)}u.displayName="MDXCreateElement"},8255:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>i,default:()=>m,frontMatter:()=>o,metadata:()=>c,toc:()=>l});var r=n(7462),a=(n(7294),n(3905));const o={},i="CheckpointConfig",c={unversionedId:"config_classes/checkpoint_config",id:"config_classes/checkpoint_config",title:"CheckpointConfig",description:"The checkpoint configuration is the entry point for the restonomer framework.",source:"@site/docs/config_classes/checkpoint_config.md",sourceDirName:"config_classes",slug:"/config_classes/checkpoint_config",permalink:"/restonomer/docs/config_classes/checkpoint_config",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Run all checkpoints",permalink:"/restonomer/docs/running_checkpoints/run_all_checkpoints"},next:{title:"DataConfig",permalink:"/restonomer/docs/config_classes/data_config"}},p={},l=[],s={toc:l},f="wrapper";function m(e){let{components:t,...n}=e;return(0,a.kt)(f,(0,r.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"checkpointconfig"},"CheckpointConfig"),(0,a.kt)("p",null,"The checkpoint configuration is the entry point for the restonomer framework.\nThe restonomer framework expects you to provide checkpoint configurations to the restonomer context instance in order to\ntrigger a checkpoint."),(0,a.kt)("p",null,"The checkpoint configuration contains below config options to be provided by the user:"),(0,a.kt)("table",null,(0,a.kt)("thead",{parentName:"table"},(0,a.kt)("tr",{parentName:"thead"},(0,a.kt)("th",{parentName:"tr",align:"left"},"Config Name"),(0,a.kt)("th",{parentName:"tr",align:"center"},"Mandatory"),(0,a.kt)("th",{parentName:"tr",align:"center"},"Default Value"),(0,a.kt)("th",{parentName:"tr",align:"left"},"Description"))),(0,a.kt)("tbody",{parentName:"table"},(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"name"),(0,a.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"Unique name for your checkpoint")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"token"),(0,a.kt)("td",{parentName:"tr",align:"center"},"No"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"Token request configuration represented by ",(0,a.kt)("inlineCode",{parentName:"td"},"TokenConfig")," class")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"data"),(0,a.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"Main data request configuration represented by ",(0,a.kt)("inlineCode",{parentName:"td"},"DataConfig")," class")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"sparkConfigs"),(0,a.kt)("td",{parentName:"tr",align:"center"},"No"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"Map of spark configurations specific to the checkpoint. ",(0,a.kt)("br",null),"If the same config is also present in ",(0,a.kt)("inlineCode",{parentName:"td"},"application.conf")," file, then checkpoint specific config gets the priority.")))),(0,a.kt)("p",null,"User can provide checkpoint configuration file in HOCON format in the below format:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-hocon"},'name = "sample_postman_checkpoint"\n\ntoken = {\n  token-request = {\n    url = "http://localhost:8080/token-response-body"\n\n    authentication = {\n      type = "BearerAuthentication"\n      bearer-token = "test_token_123"\n    }\n  }\n\n  token-response-placeholder = "ResponseBody"\n}\n\ndata = {\n  data-request = {\n    url = "https://postman-echo.com/basic-auth"\n\n    authentication = {\n      type = "BasicAuthentication"\n      user-name = "postman"\n      password = "token[$.secret]"\n    }\n  }\n\n  data-response = {\n    body = {\n      type = "Text"\n      text-format = {\n        type = "JSONTextFormat"\n      }\n    }\n\n    persistence = {\n      type = "LocalFileSystem"\n      file-format = {\n        type = "ParquetFileFormat"\n      }\n      file-path = "./rest-output/"\n    }\n  }\n}\n')))}m.isMDXComponent=!0}}]);