"use strict";(self.webpackChunksite=self.webpackChunksite||[]).push([[8362],{3905:(e,t,n)=>{n.d(t,{Zo:()=>u,kt:()=>d});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function p(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var l=a.createContext({}),s=function(e){var t=a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},u=function(e){var t=s(e.components);return a.createElement(l.Provider,{value:t},e.children)},g="mdxType",c={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},m=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,o=e.originalType,l=e.parentName,u=p(e,["components","mdxType","originalType","parentName"]),g=s(n),m=r,d=g["".concat(l,".").concat(m)]||g[m]||c[m]||o;return n?a.createElement(d,i(i({ref:t},u),{},{components:n})):a.createElement(d,i({ref:t},u))}));function d(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var o=n.length,i=new Array(o);i[0]=m;var p={};for(var l in t)hasOwnProperty.call(t,l)&&(p[l]=t[l]);p.originalType=e,p[g]="string"==typeof e?e:r,i[1]=p;for(var s=2;s<o;s++)i[s]=n[s];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}m.displayName="MDXCreateElement"},2525:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>l,contentTitle:()=>i,default:()=>c,frontMatter:()=>o,metadata:()=>p,toc:()=>s});var a=n(7462),r=(n(7294),n(3905));const o={},i="Page Number With Total Pages Based Pagination",p={unversionedId:"pagination/page_number_with_total_pages_pagination",id:"pagination/page_number_with_total_pages_pagination",title:"Page Number With Total Pages Based Pagination",description:"In this pagination mechanism, the huge datasets are split into multiple pages.",source:"@site/docs/pagination/page_number_with_total_pages_pagination.md",sourceDirName:"pagination",slug:"/pagination/page_number_with_total_pages_pagination",permalink:"/restonomer/docs/pagination/page_number_with_total_pages_pagination",draft:!1,tags:[],version:"current",frontMatter:{},sidebar:"tutorialSidebar",previous:{title:"Offset Based Pagination",permalink:"/restonomer/docs/pagination/offset_based_pagination"},next:{title:"Page Number With Total Records Based Pagination",permalink:"/restonomer/docs/pagination/page_number_with_total_records_pagination"}},l={},s=[],u={toc:s},g="wrapper";function c(e){let{components:t,...n}=e;return(0,r.kt)(g,(0,a.Z)({},u,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("h1",{id:"page-number-with-total-pages-based-pagination"},"Page Number With Total Pages Based Pagination"),(0,r.kt)("p",null,"In this pagination mechanism, the huge datasets are split into multiple pages."),(0,r.kt)("p",null,"The Rest API returns you a response with partial datasets in a single request and then again consecutive requests needs to be triggered in order to get the complete dataset."),(0,r.kt)("p",null,"Along with the dataset, the api provides other details that are required in order to fetch the next page, such as:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"Total number of pages in the complete response"),(0,r.kt)("li",{parentName:"ul"},"Current page number")),(0,r.kt)("p",null,"Apart from above details, user should be aware of the below details:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"The query param name for the next page")),(0,r.kt)("p",null,"Once we know above details, user can configure pagination in the below manner. Consider we are getting below response\nfrom API:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "data": {\n    "total": {\n      "numberPages": 3\n    },\n    "page": 1,\n    "items": [\n      {\n        "col_A": "val_1",\n        "col_B": "val_2",\n        "col_C": "val_3"\n      }\n    ]\n  }\n}\n')),(0,r.kt)("p",null,"In the above response:"),(0,r.kt)("ul",null,(0,r.kt)("li",{parentName:"ul"},"the total number of pages in the complete dataset is represented by ",(0,r.kt)("inlineCode",{parentName:"li"},"data.total.numberPages")),(0,r.kt)("li",{parentName:"ul"},"the current page number is represented by ",(0,r.kt)("inlineCode",{parentName:"li"},"data.page")),(0,r.kt)("li",{parentName:"ul"},"the query param name for next page is ",(0,r.kt)("inlineCode",{parentName:"li"},"page"))),(0,r.kt)("p",null,"Then, the pagination details can be captured in the checkpoint file in the below manner:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-hocon"},'    pagination = {\n      type = "PageNumberWithTotalPagesBasedPagination"\n      total-number-of-pages-attribute = "$.data.total.numberPages"\n      current-page-number-attribute = "$.data.page"\n    }\n')),(0,r.kt)("p",null,"The ",(0,r.kt)("inlineCode",{parentName:"p"},"total-number-of-pages-attribute")," and ",(0,r.kt)("inlineCode",{parentName:"p"},"current-page-number-attribute")," are represented as ",(0,r.kt)("a",{parentName:"p",href:"https://support.smartbear.com/alertsite/docs/monitors/api/endpoint/jsonpath.html"},"JsonPath"),"."),(0,r.kt)("p",null,"The complete example of checkpoint file including pagination is:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-hocon"},'name = "checkpoint_page_number_with_total_pages_based_pagination"\n\ndata = {\n  data-request = {\n    url = "http://localhost:8080/page_number_with_total_pages_based_pagination"\n  }\n\n  data-response = {\n    body = {\n      type = "Text"\n      text-format = {\n        type = "JSONTextFormat"\n        data-column-name = "data.items"\n      }\n    }\n\n    pagination = {\n      type = "PageNumberWithTotalPagesBasedPagination"\n      total-number-of-pages-attribute = "$.data.total.numberPages"\n      current-page-number-attribute = "$.data.page"\n    }\n\n    persistence = {\n      type = "LocalFileSystem"\n      file-format = {\n        type = "ParquetFileFormat"\n      }\n      file-path = "/tmp/pagination"\n    }\n  }\n}\n')))}c.isMDXComponent=!0}}]);