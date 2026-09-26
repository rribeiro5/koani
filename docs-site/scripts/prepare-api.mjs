import {spawnSync} from 'node:child_process';
import {cpSync, existsSync, mkdirSync, rmSync} from 'node:fs';
import path from 'node:path';
import {fileURLToPath} from 'node:url';

const siteDirectory = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const repositoryDirectory = path.resolve(siteDirectory, '..');
const gradleWrapper = path.join(
  repositoryDirectory,
  process.platform === 'win32' ? 'gradlew.bat' : 'gradlew',
);
const gradleResult = spawnSync(gradleWrapper, ['dokkaGeneratePublicationHtml'], {
  cwd: repositoryDirectory,
  stdio: 'inherit',
  shell: process.platform === 'win32',
});

if (gradleResult.error) {
  throw gradleResult.error;
}

if (gradleResult.status !== 0) {
  process.exit(gradleResult.status ?? 1);
}

const dokkaOutput = path.join(repositoryDirectory, 'build', 'dokka', 'html');
const apiOutput = path.join(siteDirectory, 'static', 'api');

if (!existsSync(path.join(dokkaOutput, 'index.html'))) {
  throw new Error(`Dokka HTML output was not found at ${dokkaOutput}`);
}

rmSync(apiOutput, {recursive: true, force: true});
mkdirSync(path.dirname(apiOutput), {recursive: true});
cpSync(dokkaOutput, apiOutput, {recursive: true});
