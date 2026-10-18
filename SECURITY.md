# Security Policy

We take the security of Koani seriously. Thank you for helping keep this project and its users safe.

## Supported Versions

Only the latest major version of Koani receives active security updates and patches.

| Version | Supported          |
| ------- | ------------------ |
| 2.x     | :white_check_mark: |

## Reporting a Vulnerability

If you discover a security vulnerability within Koani, please **do not** report it publicly through GitHub Issues or public pull requests.

Instead, please report security issues responsibly:

1. **Email or Private Report:** Submit a private security advisory via GitHub Security Advisories or contact the maintainers directly through rribeiro5.dev@proton.me.
2. **Include Details:**
   - A description of the vulnerability and potential impact.
   - Steps to reproduce or a minimal proof-of-concept.
   - Affected modules or target platforms (Android, iOS, JVM, JS, WasmJS).

## Response & Disclosure Process

- **Acknowledgment:** We will acknowledge receipt of your vulnerability report within 48 hours.
- **Assessment:** We will assess the impact and work on a fix as quickly as possible.
- **Fix & Release:** Once a patch is ready, we will release a security update and publish a security advisory detailing the issue and remediation.

## Security Best Practices for Consumers

When using Koani in your applications:

- **Protect API Credentials:** Never hardcode MyAnimeList Client Secrets or OAuth access/refresh tokens in public source code repositories or client-side application code.
- **Use Secure Persistence:** When storing tokens on-device, use secure persistence mechanisms (such as `koani-auth-persistence-ksafe` or platform Keystores/Keychain).
- **Keep Up-to-Date:** Regularly update Koani to the latest release to receive bug fixes and security improvements.
