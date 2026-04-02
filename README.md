# Hibernate Tools for Eclipse

Hibernate Tools is a set of Eclipse plugins that provides tooling for Hibernate and JPA projects, including support for mapping files, JPA annotations, HQL query prototyping, and code generation.

## Building

Requirements:
- Java 17+
- Maven 3.9+

To build:

    $ mvn clean verify

To build without running tests:

    $ mvn clean verify -DskipTests=true

## Installing

After a successful build, a p2 update site is generated under `site/target/repository/`.

To install in Eclipse:

1. Open **Help > Install New Software...**
2. Click **Add... > Local...**
3. Browse to the `site/target/repository/` directory
4. Select the Hibernate Tools features and complete the installation

## Contributing

Hibernate Tools for Eclipse is open source, and we welcome contributions!

If you want to fix a bug or make any changes, please open an issue in the [GitHub issue tracker](https://github.com/hibernate/hibernate-tools-eclipse/issues) describing the bug or new feature. We recommend making changes on a topic branch:

    $ git checkout -b my-fix

After your changes are ready and the build passes (with tests), push your branch and open a pull request for review.

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
