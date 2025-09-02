# tic-tac-toe.java

This is an implementation of tic-tac-toe in Java. See [goldenstein64/tic-tac-toe.spec](https://github.com/goldenstein64/tic-tac-toe.spec) for a specification.

This uses Gradle as its build system.

## Usage

```bash
gradle run --console plain
```

## Testing

```bash
gradle test
```

## Exporting

```bash
# export core
gradle lib:jar
# output can be found in ./lib/build/libs

# export console
gradle app:distZip
# output can be found in ./app/build/distributions
```