import {readFileSync} from 'node:fs';
import path from 'node:path';
import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const baseUrl = '/koani/';
const versionCatalogPath = path.resolve(__dirname, '..', 'gradle', 'libs.versions.toml');
const versionCatalog = readFileSync(versionCatalogPath, 'utf8');
const libraryVersion = versionCatalog.match(/^koani-version\s*=\s*"([^"]+)"$/m)?.[1];

if (!libraryVersion) {
  throw new Error(`Could not find koani-version in ${versionCatalogPath}`);
}

const config: Config = {
  title: 'Koani',
  tagline: `Kotlin Multiplatform library - v${libraryVersion}`,
  url: 'https://rribeiro5.github.io',
  baseUrl,
  organizationName: 'rribeiro5',
  projectName: 'koani',
  customFields: {
    libraryVersion,
  },

  // The generated Dokka pages live in static files, which Docusaurus cannot validate as routes.
  onBrokenLinks: 'warn',
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          editUrl: 'https://github.com/rribeiro5/koani/edit/main/docs-site/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themes: [
    [
      require.resolve('@easyops-cn/docusaurus-search-local'),
      {
        hashed: true,
        indexBlog: false,
        indexPages: false,
        language: ['en'],
        searchBarPosition: 'right',
      },
    ],
  ],

  themeConfig: {
    colorMode: {
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: `Koani v${libraryVersion}`,
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docsSidebar',
          position: 'left',
          label: 'Documentation',
        },
        {
          href: `${baseUrl}api/index.html`,
          label: 'API reference',
          position: 'left',
          target: '_blank',
          rel: 'noopener noreferrer',
        },
        {
          to: '/changelog',
          label: 'Changelog',
          position: 'left',
        },
        {
          href: 'https://github.com/rribeiro5/koani',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Koani',
          items: [
            {
              label: 'Documentation',
              to: '/docs/getting-started/quickstart',
            },
            {
              label: 'API reference',
              href: `${baseUrl}api/index.html`,
              target: '_blank',
              rel: 'noopener noreferrer',
            },
            {
              label: 'Changelog',
              to: '/changelog',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/rribeiro5/koani',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Koani contributors. Built with Docusaurus.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
