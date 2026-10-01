import type {ReactNode} from 'react';
import Link from '@docusaurus/Link';
import useBaseUrl from '@docusaurus/useBaseUrl';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import CodeBlock from '@theme/CodeBlock';
import styles from './index.module.css';

const codeExample = `// 1. Initialize KoaniClient
val client = KoaniClient.Builder(clientId = "YOUR_CLIENT_ID").build()

// 2. Fetch anime details requesting only TITLE and MEAN fields
val anime = client.anime.getAnimeDetails(
    animeId = 5114,
    fields = listOf(
        AnimeField.TITLE,
        AnimeField.MEAN
    )
)

println("\${anime.title} — Score: \${anime.mean}")`;

export default function Home(): ReactNode {
  const {siteConfig} = useDocusaurusContext();
  const libraryVersion = (siteConfig.customFields?.libraryVersion as string) || '2.0.0';
  const installSnippet = `implementation("io.github.rribeiro5:koani-core:${libraryVersion}")`;
  const apiReferenceUrl = useBaseUrl('/api/index.html');

  return (
    <Layout
      title="Koani - Kotlin Multiplatform MyAnimeList Client"
      description="Documentation and API reference for Koani, a Kotlin Multiplatform library for the MyAnimeList API.">

      {/* Hero Banner */}
      <header className={`hero hero--primary ${styles.heroBanner}`}>
        <div className="container">
          <h1 className="hero__title">Koani</h1>
          <p className="hero__subtitle">
            Kotlin Multiplatform library for the MyAnimeList API
          </p>

          <div className={styles.heroInstall}>
            <CodeBlock language="kotlin">{installSnippet}</CodeBlock>
          </div>

          <div className={styles.buttons}>
            <Link
              className="button button--secondary button--lg"
              to="/docs/getting-started/quickstart">
              🚀 Get Started
            </Link>
            <a
              className="button button--outline button--secondary button--lg"
              href={apiReferenceUrl}
              target="_blank"
              rel="noopener noreferrer">
              API Reference
            </a>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main>
        {/* Key Features Grid */}
        <section className={styles.section}>
          <div className="container">
            <div className="text--center margin-bottom--lg">
              <h2>Key Features</h2>
            </div>
            <div className="row">
              <div className="col col--3 margin-bottom--md">
                <div className={`card padding--lg text--center ${styles.featureCard}`}>
                  <div className={styles.featureIcon}>🌍</div>
                  <h3>Multiplatform First</h3>
                  <p>
                    Share your MyAnimeList integration code seamlessly across Android, iOS, Desktop, WebAssembly, and JS targets.
                  </p>
                </div>
              </div>
              <div className="col col--3 margin-bottom--md">
                <div className={`card padding--lg text--center ${styles.featureCard}`}>
                  <div className={styles.featureIcon}>🔒</div>
                  <h3>OAuth2 &amp; PKCE</h3>
                  <p>
                    Built-in user authorization, automatic token refresh, and encrypted persistence support via KSafe.
                  </p>
                </div>
              </div>
              <div className="col col--3 margin-bottom--md">
                <div className={`card padding--lg text--center ${styles.featureCard}`}>
                  <div className={styles.featureIcon}>🎯</div>
                  <h3>Type-Safe Fields</h3>
                  <p>
                    Request exact fields (such as <code>AnimeField.TITLE</code> and <code>AnimeField.MEAN</code>) to optimize payload sizes.
                  </p>
                </div>
              </div>
              <div className="col col--3 margin-bottom--md">
                <div className={`card padding--lg text--center ${styles.featureCard}`}>
                  <div className={styles.featureIcon}>⚡</div>
                  <h3>Async Coroutines</h3>
                  <p>
                    Powered by Ktor HTTP client and Kotlin coroutines with resilient JSON serialization.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Code Preview Section */}
        <section className={styles.sectionAlt}>
          <div className="container">
            <div className="row row--align-center">
              <div className="col col--5 margin-bottom--md">
                <h2>Simple &amp; Expressive API</h2>
                <p>
                  Koani makes querying MyAnimeList data intuitive. Configure credentials with <code>KoaniClient.Builder</code>, specify the fields your UI needs, and execute requests asynchronously.
                </p>
                <p>
                  Built with defensive JSON parsing so your application remains unaffected when MyAnimeList adds new fields to API responses.
                </p>
              </div>
              <div className="col col--7 margin-bottom--md">
                <div className={styles.codePreview}>
                  <CodeBlock language="kotlin">{codeExample}</CodeBlock>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>
    </Layout>
  );
}
