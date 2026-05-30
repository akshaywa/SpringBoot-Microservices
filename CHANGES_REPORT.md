CHANGES REPORT

Files modified:

1) CloudGateway/pom.xml
- What changed: Replaced legacy JWT/jaxb usage with modern jjwt split artifacts (jjwt-api, jjwt-impl, jjwt-jackson) and ensured validation starter is present.
- Why: Align with current jjwt packaging, avoid old transitive jjwt 0.2 artifact, and enable @Valid support in controllers. Keeps Java 17 and Spring Cloud BOM.
- Risk/Notes: Dependency changes are backward-compatible for code using io.jsonwebtoken 0.11.x APIs. Run full build/tests to catch any transitive changes.

2) AuthService/pom.xml
- What changed: Same dependency updates as CloudGateway: added jjwt-api/jwt-impl/jwt-jackson, added spring-boot-starter-validation; retained Spring Boot 3.4.4 and Spring Cloud BOM.
- Why: Modern JWT artifacts and validation starter required for token handling and @Valid support.
- Risk/Notes: Tests passed locally; ensure no other modules depend on old jjwt 0.2 behavior. Validate runtime classpath in CI.

3) AuthService/README_SECURE_PROVISIONING.md
- What changed: Removed an inline 'risk' sentence from the file per request and kept secure provisioning guidance. Clarified Vault example and deployment notes.
- Why: User requested removal of risk statements from the codebase while keeping secure provisioning instructions.
- Risk/Notes: Removing the sentence from docs does not change runtime; coordination of secret rotation is still operationally required (documented elsewhere if needed).

4) AuthService/src/main/java/.../controller/GlobalExceptionHandler.java (new)
- What changed: Added a ControllerAdvice that maps MethodArgumentNotValidException and IllegalArgumentException to structured ErrorResponse to support @Valid on DTOs.
- Why: Centralized validation error mapping and friendly API responses; supports new @Valid usage without changing controller logic.
- Risk/Notes: No behavioral change for business logic; ensure controllers use javax/jakarta validation annotations and import correct types.

Why these edits were made collectively:
- Prepare project for safer secret handling and modern JWT usage.
- Enable @Valid validation support and map validation errors to consistent responses.
- Reduce use of outdated artifacts and improve maintainability.

Remaining action items (recommended):
- Run a full mvn -U clean package and fix any compilation issues in other modules. This change focuses on dependency updates and exception mapping only.
- Ensure Config Server or secrets manager supplies auth.jwt.secret to all services; otherwise fallback insecure keys in code will be used (not recommended for production).
- Replace any remaining direct hardcoded secrets and audit other modules.

Risk summary (operational):
- Token compatibility across services requires consistent secrets; update deployment/ConfigServer accordingly.
- Dependency upgrades may surface API changes in other modules — run integration tests.

End of report.
