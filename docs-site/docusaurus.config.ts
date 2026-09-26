import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const baseUrl = '/koani/';

const config: Config = {
  title: 'Koani',
  tagline: 'Kotlin Multiplatform library',
  url: 'https://rribeiro5.github.io',
  baseUrl,
  organizationName: 'rribeiro5',
  projectName: 'koani',

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

  themeConfig: {
    colorMode: {
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: 'Koani',
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
              to: '/docs/placeholder',
            },
            {
              label: 'API reference',
              href: `${baseUrl}api/index.html`,
              target: '_blank',
              rel: 'noopener noreferrer',
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
