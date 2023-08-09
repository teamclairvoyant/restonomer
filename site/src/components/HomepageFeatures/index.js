import React from 'react';
import clsx from 'clsx';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Configuration Driven Framework',
    Svg: require('@site/static/img/undraw_docusaurus_mountain.svg').default,
    description: (
      <>
        Restonomer is a configuration-driven framework that hides all the implementation details from users and provides just an abstraction for building integration layers to consume HTTP-based services.
      </>
    ),
  },
  {
    title: 'Data Ingestion',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        Restonomer enables users to ingest data from HTTP-based services in a distributed manner.
      </>
    ),
  },
  {
    title: 'Response Data Formats',
    Svg: require('@site/static/img/undraw_docusaurus_tree.svg').default,
    description: (
      <>
        Restonomer supports various API response data formats like JSON, XML, CSV, etc.
      </>
    ),
  },
  {
    title: 'Data Transformation',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Restonomer provides users with the ability to transform their api response data to the required format and structure in a distributed fashion.
        Restonomer makes use of the spark functions to transform the data in the background. 
        User need not to write any code for applying transformation, they just need to provide required configurations in the most easy way possible.
      </>
    ),
  },
  {
    title: 'Data Persistence',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Restonomer provides users with the ability to persist the api response data in the target storage system of their choice.
        Once the data has been fetched and transformed, user can provide configurations specifying the target destination and other required parameters related to the target system.
      </>
    ),
  },
  {
    title: 'Authentication',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        With Restonomer, user does not have to worry about implementing the algorithm for authenticating itself to the REST API.
        Restonomer is completely configuration driven where users just need to mention the authentication type and provide the credentials and the rest is taken care by the application itself.
      </>
    ),
  },
  {
    title: 'Fetch Selected Data',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Restonomer provides a mechanism to deal with over-fetching of data. 
        Suppose an API returns 100 fields in a response, but we are interested only in 10 fields. 
        Then, without requesting any change on the API side implementation, users can configure the fields that they are really interested in. 
        This reduces the amount of data size to be fetched across the network for further processing.
      </>
    ),
  },
  {
    title: 'Pagination',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Restomer provides support for pagination while fetching huge datasets from the REST API. 
        Some of the APIs are not able to fetch the complete data in a single request, and hence they make use of pagination to load the data in consecutive pages. 
        A separate http request gets created for each page. 
        Each API has its own custom pagination scheme, and Restonomer internally implements the solution to deal with the same. 
        The users just need to provide few configurations without letting themselves know about the internal details of the implementation.
      </>
    ),
  },
  {
    title: 'Auto Retry',
    Svg: require('@site/static/img/undraw_docusaurus_react.svg').default,
    description: (
      <>
        Restonomer makes sure that its auto retry mechanism makes another attempt to retrieve the relevant information in the event of a request failure, depending on the type of status code it receives as a response. 
        Restonomer is robust and able to deal with potential temporary failures of the services you rely on.
      </>
    ),
  },
];

function Feature({Svg, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <h3>{title}</h3>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function HomepageFeatures() {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
