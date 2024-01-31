"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[9533],{3905:(e,t,n)=>{n.d(t,{Zo:()=>s,kt:()=>d});var r=n(7294);function o(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function a(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){o(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var p=r.createContext({}),l=function(e){var t=r.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):a(a({},t),e)),n},s=function(e){var t=l(e.components);return r.createElement(p.Provider,{value:t},e.children)},u="mdxType",f={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},m=r.forwardRef((function(e,t){var n=e.components,o=e.mdxType,i=e.originalType,p=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),u=l(n),m=o,d=u["".concat(p,".").concat(m)]||u[m]||f[m]||i;return n?r.createElement(d,a(a({ref:t},s),{},{components:n})):r.createElement(d,a({ref:t},s))}));function d(e,t){var n=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var i=n.length,a=new Array(i);a[0]=m;var c={};for(var p in t)hasOwnProperty.call(t,p)&&(c[p]=t[p]);c.originalType=e,c[u]="string"==typeof e?e:o,a[1]=c;for(var l=2;l<i;l++)a[l]=n[l];return r.createElement.apply(null,a)}return r.createElement.apply(null,n)}m.displayName="MDXCreateElement"},6035:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>a,default:()=>f,frontMatter:()=>i,metadata:()=>c,toc:()=>l});var r=n(7462),o=(n(7294),n(3905));const i={sidebar_position:3},a="Application Configurations",c={unversionedId:"restonomer_context/application_configurations",id:"restonomer_context/application_configurations",title:"Application Configurations",description:"Application configurations are high level configs that application requires in order to behave in a specific way.",source:"@site/docs/restonomer_context/application_configurations.md",sourceDirName:"restonomer_context",slug:"/restonomer_context/application_configurations",permalink:"/restonomer/docs/restonomer_context/application_configurations",draft:!1,tags:[],version:"current",sidebarPosition:3,frontMatter:{sidebar_position:3},sidebar:"tutorialSidebar",previous:{title:"Checkpoints",permalink:"/restonomer/docs/restonomer_context/checkpoints"},next:{title:"Config Variables",permalink:"/restonomer/docs/restonomer_context/config_variables"}},p={},l=[],s={toc:l},u="wrapper";function f(e){let{components:t,...n}=e;return(0,o.kt)(u,(0,r.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"application-configurations"},"Application Configurations"),(0,o.kt)("p",null,"Application configurations are high level configs that application requires in order to behave in a specific way."),(0,o.kt)("p",null,"Application configurations are represented by a case class ",(0,o.kt)("inlineCode",{parentName:"p"},"ApplicationConfig"),"."),(0,o.kt)("p",null,"Application configurations are provided in a file ",(0,o.kt)("inlineCode",{parentName:"p"},"application.conf")," that is kept under restonomer context directory."),(0,o.kt)("p",null,"This conf file contains all application related configurations such as spark configurations."),(0,o.kt)("p",null,"Below is the sample ",(0,o.kt)("inlineCode",{parentName:"p"},"application.conf")," file:"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-hocon"},'spark-configs = {\n  "spark.master" = "local[*]"\n}\n')))}f.isMDXComponent=!0}}]);