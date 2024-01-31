"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[3063],{3905:(e,t,n)=>{n.d(t,{Zo:()=>c,kt:()=>f});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function l(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function s(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var p=a.createContext({}),i=function(e){var t=a.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):l(l({},t),e)),n},c=function(e){var t=i(e.components);return a.createElement(p.Provider,{value:t},e.children)},m="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},u=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,o=e.originalType,p=e.parentName,c=s(e,["components","mdxType","originalType","parentName"]),m=i(n),u=r,f=m["".concat(p,".").concat(u)]||m[u]||d[u]||o;return n?a.createElement(f,l(l({ref:t},c),{},{components:n})):a.createElement(f,l({ref:t},c))}));function f(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var o=n.length,l=new Array(o);l[0]=u;var s={};for(var p in t)hasOwnProperty.call(t,p)&&(s[p]=t[p]);s.originalType=e,s[m]="string"==typeof e?e:r,l[1]=s;for(var i=2;i<o;i++)l[i]=n[i];return a.createElement.apply(null,l)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},4585:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>l,default:()=>d,frontMatter:()=>o,metadata:()=>s,toc:()=>i});var a=n(7462),r=(n(7294),n(3905));const o={},l="Cast From To Data Types",s={unversionedId:"transformation/cast_from_to_data_types",id:"transformation/cast_from_to_data_types",title:"Cast From To Data Types",description:"This transformation lets users cast all columns having X data type to a different Y data type.",source:"@site/docs/transformation/cast_from_to_data_types.md",sourceDirName:"transformation",slug:"/transformation/cast_from_to_data_types",permalink:"/restonomer/docs/transformation/cast_from_to_data_types",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Cast Columns Based On Suffix",permalink:"/restonomer/docs/transformation/cast_columns_based_on_suffix"},next:{title:"Cast Nested Column",permalink:"/restonomer/docs/transformation/cast_nested_column"}},p={},i=[],c={toc:i},m="wrapper";function d(e){let{components:t,...n}=e;return(0,r.kt)(m,(0,a.Z)({},c,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"cast-from-to-data-types"},"Cast From To Data Types"),(0,r.kt)("p",null,"This transformation lets users cast all columns having ",(0,r.kt)("inlineCode",{parentName:"p"},"X")," data type to a different ",(0,r.kt)("inlineCode",{parentName:"p"},"Y")," data type."),(0,r.kt)("p",null,"Users can provide a list of mapping for source data type and target data type."),(0,r.kt)("p",null,"Users can provide a flag to define whether the casting needs to be performed at nested level or only at the root level."),(0,r.kt)("p",null,"This transformation expects user to provide below inputs:"),(0,r.kt)("table",null,(0,r.kt)("thead",{parentName:"table"},(0,r.kt)("tr",{parentName:"thead"},(0,r.kt)("th",{parentName:"tr",align:"left"},"Input Arguments"),(0,r.kt)("th",{parentName:"tr",align:"center"},"Mandatory"),(0,r.kt)("th",{parentName:"tr",align:"center"},"Default Value"),(0,r.kt)("th",{parentName:"tr",align:"left"},"Description"))),(0,r.kt)("tbody",{parentName:"table"},(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:"left"},"data-type-mapper"),(0,r.kt)("td",{parentName:"tr",align:"center"},"Yes"),(0,r.kt)("td",{parentName:"tr",align:"center"},"-"),(0,r.kt)("td",{parentName:"tr",align:"left"},"It defines the list of mapping of source data type and target data type")),(0,r.kt)("tr",{parentName:"tbody"},(0,r.kt)("td",{parentName:"tr",align:"left"},"cast-recursively"),(0,r.kt)("td",{parentName:"tr",align:"center"},"No"),(0,r.kt)("td",{parentName:"tr",align:"center"},"false"),(0,r.kt)("td",{parentName:"tr",align:"left"},"It defines a boolean flag that tells whether casting needs to be performed ",(0,r.kt)("br",null),"at nested level (cast-recursively = true) or only at the root level (cast-recursively = false)")))),(0,r.kt)("p",null,"For example, consider we have below restonomer response in json:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "col_A": 5,\n  "col_B": 4,\n  "col_C": 3.4678,\n  "col_D": {\n    "col_E": 6\n  },\n  "col_F": [\n    {\n      "col_G": 7\n    }\n  ]\n}\n')),(0,r.kt)("p",null,"Now, suppose the requirement is to cast all columns having ",(0,r.kt)("inlineCode",{parentName:"p"},"long")," data type to ",(0,r.kt)("inlineCode",{parentName:"p"},"string")," data type and ",(0,r.kt)("inlineCode",{parentName:"p"},"double")," data type to ",(0,r.kt)("inlineCode",{parentName:"p"},"decimal(5,2)")," data type."),(0,r.kt)("p",null,"Then, user can configure the ",(0,r.kt)("inlineCode",{parentName:"p"},"CastFromToDataTypes")," transformation in the below manner:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-hocon"},'{\n  type = "CastFromToDataTypes"\n  data-type-mapper = {\n    "long" = "string"\n    "double" = "decimal(5,2)"\n  }\n  cast-recursively = true\n}\n')),(0,r.kt)("p",null,"The transformed response will now have the columns with the desired data types:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "col_A": "5",\n  "col_B": "4",\n  "col_C": 3.47,\n  "col_D": {\n    "col_E": "6"\n  },\n  "col_F": [\n    {\n      "col_G": "7"\n    }\n  ]\n}\n')))}d.isMDXComponent=!0}}]);