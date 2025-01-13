# Contributing to Tech Radar

Thank you for your interest in contributing to the Tech Radar project! This
document provides guidelines and instructions for contributing to Stage 1 - the
REST API implementation.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Process](#development-process)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)
- [Testing Guidelines](#testing-guidelines)
- [Documentation](#documentation)
- [Issue Reporting](#issue-reporting)

## Code of Conduct

We are committed to providing a welcoming and inclusive environment. All
contributors must adhere to our Code of Conduct:

- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/your-username/tech-radar.git
   ```
3. Add the upstream remote:
   ```bash
   git remote add upstream https://github.com/original-org/tech-radar.git
   ```
4. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Process

1. **Pick an Issue**: Start with existing issues or create a new one
2. **Discuss**: For significant changes, discuss in the issue first
3. **Branch**: Create a feature branch from `main`
4. **Develop**: Make your changes following our coding standards
5. **Test**: Ensure all tests pass and add new ones
6. **Document**: Update documentation as needed
7. **Submit**: Create a pull request

## Coding Standards

### Java Code Style

- Follow standard Java naming conventions
- Use meaningful names for variables, methods, and classes
- Keep methods focused and concise
- Document public APIs with Javadoc

### Code Organization

```java
package com.company.project;

import java.util.List;
import java.util.Optional;

public class ClassName {

  // Constants
  private static final String CONSTANT_NAME = "value";

  // Fields
  private final DependencyType dependency;

  // Constructors
  public ClassName(DependencyType dependency) {
    this.dependency = dependency;
  }

  // Public methods
  public void methodName() {
    // Implementation
  }

  // Private methods
  private void helperMethod() {
    // Implementation
  }
}
```

### Code Formatting

- Use 4 spaces for indentation
- Maximum line length: 120 characters
- One statement per line
- Use blank lines to separate logical blocks
- Remove unused imports and dead code

## Commit Guidelines

Follow conventional commits format:

```
type(scope): subject

body

footer
```

Types:

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code changes that neither fix bugs nor add features
- `test`: Adding or modifying tests
- `chore`: Changes to build process or auxiliary tools

Example:

```
feat(technology): add batch update endpoint

- Implement batch update controller
- Add request validation
- Include unit tests

Closes #123
```

## Pull Request Process

1. **Title**: Use the same format as commit messages
2. **Description**: Include:

- Summary of changes
- Related issue numbers
- Breaking changes
- Screenshots for UI changes
- Testing instructions

3. **Checklist**:

- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] Code formatted
- [ ] No security vulnerabilities introduced
- [ ] Changes reviewed locally

4. **Review Process**:

- Two approvals required
- All comments must be resolved
- CI checks must pass

## Testing Guidelines

### Unit Tests

- Write tests for all new code
- Follow AAA pattern (Arrange, Act, Assert)
- Use meaningful test names describing the scenario
- One assertion per test when possible

Example:

```java
@Test
void shouldReturnTechnologyWhenValidIdProvided(){
  // Arrange
  UUID id=UUID.randomUUID();
  Technology expected=createTestTechnology(id);
  when(repository.findById(id)).thenReturn(Optional.of(expected));

  // Act
  Technology actual=service.getTechnology(id);

  // Assert
  assertEquals(expected,actual);
  }
```

### Integration Tests

- Test API endpoints with real HTTP requests
- Test database operations with real database
- Use test containers for external dependencies
- Clean up test data after each test

## Documentation

- Update README.md for significant changes
- Document all new APIs using OpenAPI/Swagger
- Include code examples for new features
- Update architectural diagrams if needed

## Issue Reporting

When creating an issue, include:

1. **Description**: Clear description of the problem
2. **Steps to Reproduce**: Detailed steps to reproduce the issue
3. **Expected Behavior**: What you expected to happen
4. **Actual Behavior**: What actually happened
5. **Environment**:

- Java version
- Spring Boot version
- Database version
- OS information

6. **Additional Context**: Logs, screenshots, etc.

## Review Process

1. **Code Review**:

- Code correctness
- Test coverage
- Documentation
- Performance considerations
- Security implications

2. **Design Review** (for significant changes):

- Architecture consistency
- API design
- Data model changes
- Security considerations

## Questions or Need Help?

Feel free to:

- Open an issue for questions
- Join our community chat
- Contact the maintainers

Thank you for contributing to Tech Radar!
