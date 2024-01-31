"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[675],{3905:(e,t,n)=>{n.d(t,{Zo:()=>s,kt:()=>d});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function l(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?l(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):l(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function c(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},l=Object.keys(e);for(r=0;r<l.length;r++)n=l[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);for(r=0;r<l.length;r++)n=l[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var p=r.createContext({}),i=function(e){var t=r.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=i(e.components);return r.createElement(p.Provider,{value:t},e.children)},u="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},f=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,l=e.originalType,p=e.parentName,s=c(e,["components","mdxType","originalType","parentName"]),u=i(n),f=a,d=u["".concat(p,".").concat(f)]||u[f]||m[f]||l;return n?r.createElement(d,o(o({ref:t},s),{},{components:n})):r.createElement(d,o({ref:t},s))}));function d(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var l=n.length,o=new Array(l);o[0]=f;var c={};for(var p in t)hasOwnProperty.call(t,p)&&(c[p]=t[p]);c.originalType=e,c[u]="string"==typeof e?e:a,o[1]=c;for(var i=2;i<l;i++)o[i]=n[i];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}f.displayName="MDXCreateElement"},5538:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>o,default:()=>m,frontMatter:()=>l,metadata:()=>c,toc:()=>i});var r=n(7462),a=(n(7294),n(3905));const l={},o="Replace String In Column Value",c={unversionedId:"transformation/replace_string_in_column_value",id:"transformation/replace_string_in_column_value",title:"Replace String In Column Value",description:"It lets the user replace the pattern in the column specified by user.",source:"@site/docs/transformation/replace_string_in_column_value.md",sourceDirName:"transformation",slug:"/transformation/replace_string_in_column_value",permalink:"/restonomer/docs/transformation/replace_string_in_column_value",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Replace String In Column Name",permalink:"/restonomer/docs/transformation/replace_string_in_column_name"},next:{title:"Select Columns",permalink:"/restonomer/docs/transformation/select_columns"}},p={},i=[],s={toc:i},u="wrapper";function m(e){let{components:t,...n}=e;return(0,a.kt)(u,(0,r.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"replace-string-in-column-value"},"Replace String In Column Value"),(0,a.kt)("p",null,"It lets the user replace the pattern in the column specified by user."),(0,a.kt)("p",null,"This transformation expects user to provide below inputs:"),(0,a.kt)("table",null,(0,a.kt)("thead",{parentName:"table"},(0,a.kt)("tr",{parentName:"thead"},(0,a.kt)("th",{parentName:"tr",align:"left"},"Input Arguments"),(0,a.kt)("th",{parentName:"tr",align:"center"},"Mandatory"),(0,a.kt)("th",{parentName:"tr",align:"center"},"Default Value"),(0,a.kt)("th",{parentName:"tr",align:"left"},"Description"))),(0,a.kt)("tbody",{parentName:"table"},(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"column-name"),(0,a.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"It is the column name")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"pattern"),(0,a.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"The values that needs to be replaced")),(0,a.kt)("tr",{parentName:"tbody"},(0,a.kt)("td",{parentName:"tr",align:"left"},"replacement"),(0,a.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,a.kt)("td",{parentName:"tr",align:"center"},"-"),(0,a.kt)("td",{parentName:"tr",align:"left"},"The value that replaces the pattern")))),(0,a.kt)("p",null,"For example, consider we have below restonomer response in json:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "col_A": 5,\n  "col_B": 4,\n  "col_C": 3.4678\n}\n')),(0,a.kt)("p",null,"Now, suppose the requirement is to replace the col_A column values :"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-text"},'col_A -> "abc"\ncol_B -> 4\ncol_C -> 3.4678\n')),(0,a.kt)("p",null,"Then, user can configure the ",(0,a.kt)("inlineCode",{parentName:"p"},"ReplaceStringInColumnValue")," transformation in the below manner:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-hocon"},'{\n  type = "ReplaceStringInColumnValue"\n  column-name = "col_A"\n  pattern = 5\n  replacement = "abc"\n  }\n')),(0,a.kt)("p",null,"The transformed response will have the replaced value or pattern in the desired column as shown below."),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "col_A": "abc",\n  "col_B": 4,\n  "col_C": 3.4678\n}\n')))}m.isMDXComponent=!0}}]);