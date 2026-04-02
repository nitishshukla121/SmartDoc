# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.5/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.5/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/4.0.5/reference/using/devtools.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/4.0.5/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.5/reference/web/spring-security.html)
* [In-memory Chat Memory Repository](https://docs.spring.io/spring-ai/reference/api/chat-memory.html)
* [Ollama](https://docs.spring.io/spring-ai/reference/api/chat/ollama-chat.html)
* [OpenAI](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)
* [Tika Document Reader](https://docs.spring.io/spring-ai/reference/api/etl-pipeline.html#_tika_docx_pptx_html)
* [Chroma Vector Database](https://docs.spring.io/spring-ai/reference/api/vectordbs/chroma.html)
* [Text embedding reference](https://docs.spring.io/spring-ai/reference/api/embeddings/vertexai-embeddings-text.html)
* [Multimodal embedding reference](https://docs.spring.io/spring-ai/reference/api/embeddings/vertexai-embeddings-multimodal.html)
* [Vertex AI Gemini](https://docs.spring.io/spring-ai/reference/api/chat/vertexai-gemini-chat.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/4.0.5/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.5/reference/web/servlet.html)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/4.0.5/reference/web/reactive.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* chroma: [`chromadb/chroma:latest`](https://hub.docker.com/r/chromadb/chroma)
* mysql: [`mysql:latest`](https://hub.docker.com/_/mysql)
* ollama: [`ollama/ollama:latest`](https://hub.docker.com/r/ollama/ollama)

Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

