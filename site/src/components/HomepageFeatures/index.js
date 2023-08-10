import React from 'react';
import clsx from 'clsx';
import styles from './styles.module.css';

const FeatureList = [
  {
    title: 'Configuration Driven Framework',
    imgSrc: require('@site/static/img/configuration_driven.png').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer is a configuration-driven framework that hides all the implementation details from users and provides just an abstraction for building integration layers to consume HTTP-based services.
      </>
    ),
  },
  {
    title: 'Data Ingestion',
    imgSrc: require('@site/static/img/data_ingestion.png').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer enables users to ingest data from HTTP-based services in a distributed manner.
      </>
    ),
  },
  {
    title: 'Response Data Formats',
    imgSrc: require('@site/static/img/response_formats.png').default,
    width: '250',
    height: '100',
    description: (
      <>
        Restonomer supports various API response data formats like JSON, XML, CSV, etc.
      </>
    ),
  },
  {
    title: 'Data Transformation',
    imgSrc: require('@site/static/img/data_transformation.png').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer provides users with the ability to transform their api response data to the required format and structure in a distributed fashion.
      </>
    ),
  },
  {
    title: 'Data Persistence',
    imgSrc: require('@site/static/img/data_persistence.png').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer provides users with the ability to persist the api response data in the target storage system of their choice.
      </>
    ),
  },
  {
    title: 'Authentication',
    imgSrc: require('@site/static/img/authentication.png').default,
    width: '500',
    height: '100',
    description: (
      <>
        With Restonomer, user does not have to worry about implementing the algorithm for authenticating itself to the REST API.
        Restonomer is completely configuration driven where users just need to mention the authentication type and provide the credentials and the rest is taken care by the application itself.
      </>
    ),
  },
  {
    title: 'Fetch Selected Data',
    imgSrc: require('@site/static/img/fetch_selected_data.png').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer provides a mechanism to deal with over-fetching of data. 
        Without requesting any change on the API side implementation, users can configure the fields that they are really interested in. 
        This reduces the amount of data size to be fetched across the network for further processing.
      </>
    ),
  },
  {
    title: 'Pagination',
    imgSrc: require('@site/static/img/pagination.png').default,
    width: '200',
    height: '100',
    description: (
      <>
        Restomer provides support for pagination while fetching huge datasets from the REST API. 
        Each API has its own custom pagination scheme, and Restonomer internally implements the solution to deal with the same. 
      </>
    ),
  },
  {
    title: 'Auto Retry',
    imgSrc: require('@site/static/img/retry.jpg').default,
    width: '100',
    height: '100',
    description: (
      <>
        Restonomer makes sure that its auto retry mechanism makes another attempt to retrieve the relevant information in the event of a request failure, depending on the type of status code it receives as a response. 
      </>
    ),
  },
];

function Feature({imgSrc, width, height, title, description}) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <img src={imgSrc} width={width} height={height}/>
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
