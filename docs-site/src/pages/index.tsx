import type {ReactNode} from 'react';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import Layout from '@theme/Layout';
import styles from './index.module.css';

export default function Home(): ReactNode {
  const apiReferenceUrl = useBaseUrl('/api/index.html');

  return (
    <Layout
      title="Koani"
      description="Documentation and API reference for the Koani Kotlin Multiplatform library.">
      <header className={`hero hero--primary ${styles.heroBanner}`}>
        <div className="container">
          <h1 className="hero__title">Koani</h1>
          <p className="hero__subtitle">Kotlin Multiplatform library</p>
          <div className={styles.buttons}>
            <Link
              className="button button--secondary button--lg"
              to="/docs/placeholder">
              Documentation
            </Link>
            <a
              className="button button--secondary button--lg"
              href={apiReferenceUrl}
              target="_blank"
              rel="noopener noreferrer">
              API reference
            </a>
          </div>
        </div>
      </header>
    </Layout>
  );
}
