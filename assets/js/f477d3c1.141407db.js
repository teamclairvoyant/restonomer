"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[2379],{3905:(e,t,r)=>{r.d(t,{Zo:()=>l,kt:()=>f});var n=r(7294);function o(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?a(Object(r),!0).forEach((function(t){o(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):a(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function c(e,t){if(null==e)return{};var r,n,o=function(e,t){if(null==e)return{};var r,n,o={},a=Object.keys(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||(o[r]=e[r]);return o}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(o[r]=e[r])}return o}var p=n.createContext({}),s=function(e){var t=n.useContext(p),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},l=function(e){var t=s(e.components);return n.createElement(p.Provider,{value:t},e.children)},m="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,o=e.mdxType,a=e.originalType,p=e.parentName,l=c(e,["components","mdxType","originalType","parentName"]),m=s(r),d=o,f=m["".concat(p,".").concat(d)]||m[d]||u[d]||a;return r?n.createElement(f,i(i({ref:t},l),{},{components:r})):n.createElement(f,i({ref:t},l))}));function f(e,t){var r=arguments,o=t&&t.mdxType;if("string"==typeof e||o){var a=r.length,i=new Array(a);i[0]=d;var c={};for(var p in t)hasOwnProperty.call(t,p)&&(c[p]=t[p]);c.originalType=e,c[m]="string"==typeof e?e:o,i[1]=c;for(var s=2;s<a;s++)i[s]=r[s];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},3563:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>p,contentTitle:()=>i,default:()=>u,frontMatter:()=>a,metadata:()=>c,toc:()=>s});var n=r(7462),o=(r(7294),r(3905));const a={},i="Simple Client Application",c={unversionedId:"getting_started/simple_restonomer_client_application",id:"getting_started/simple_restonomer_client_application",title:"Simple Client Application",description:"* Import the RestonomerContext class:",source:"@site/docs/getting_started/simple_restonomer_client_application.md",sourceDirName:"getting_started",slug:"/getting_started/simple_restonomer_client_application",permalink:"/restonomer/docs/getting_started/simple_restonomer_client_application",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Add SBT Dependency",permalink:"/restonomer/docs/getting_started/add_sbt_dependency"},next:{title:"Restonomer Context Directory",permalink:"/restonomer/docs/restonomer_context/restonomer_context_directory"}},p={},s=[],l={toc:s},m="wrapper";function u(e){let{components:t,...r}=e;return(0,o.kt)(m,(0,n.Z)({},l,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"simple-client-application"},"Simple Client Application"),(0,o.kt)("ul",null,(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("p",{parentName:"li"},"Import the ",(0,o.kt)("inlineCode",{parentName:"p"},"RestonomerContext")," class:"),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"import com.clairvoyant.restonomer.app.RestonomerContext\n"))),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("p",{parentName:"li"},"Create ",(0,o.kt)("inlineCode",{parentName:"p"},"RestonomerContext")," instance:"),(0,o.kt)("p",{parentName:"li"},"User can create the restonomer context instance by passing the restonomer context directory path to the constructor\nof RestonomerContext class."),(0,o.kt)("p",{parentName:"li"},"Currently, user can provide the local file system path or GCS path for the restonomer context directory."),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre",className:"language-scala"},'private val restonomerContextDirectoryPath = "<restonomer_context_directory_path>"\nprivate val restonomerContext = RestonomerContext(restonomerContextDirectoryPath)\n'))),(0,o.kt)("li",{parentName:"ul"},(0,o.kt)("p",{parentName:"li"},"Run Checkpoints:"),(0,o.kt)("p",{parentName:"li"},"Once the restonomer context instance is created, user can use various methods provided by the instance to run specific\ncheckpoints or all checkpoints as desired."),(0,o.kt)("pre",{parentName:"li"},(0,o.kt)("code",{parentName:"pre",className:"language-scala"},"restonomerContext.runAllCheckpoints()\n")))))}u.isMDXComponent=!0}}]);