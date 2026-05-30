# Secure provisioning of auth.jwt.secret

- Do NOT store plaintext secrets in the public Config Server repository.
- Use a secrets manager such as HashiCorp Vault, AWS Secrets Manager, Azure Key Vault, or GitHub Secrets.
- For Spring Cloud Config + Vault integration, enable the Vault backend and reference the secret as `auth.jwt.secret` in application properties.
- If you must store in Git, use encrypted values with Spring Cloud Config's symmetric/asymmetric encryption features (encrypt the value and store the cipher text). Keep the decryption key securely managed by the Config Server host.
- Restrict access to the Config repository; enable branch protection and require PR reviews to change secrets.
- Example using Vault (high-level):
  1) Store secret: `vault kv put secret/auth-service auth.jwt.secret="<base64-or-random-secret>"`
  2) Configure Config Server bootstrap with Vault properties and token.
  3) Ensure applications use Spring Cloud Config to fetch `auth.jwt.secret` and have `@RefreshScope` on beans that use it.

- When deploying, set `auth.jwt.secret` via environment variables or platform secret stores (K8s secrets, Cloud provider secret maps). Do not embed secrets in Docker images or container images.
