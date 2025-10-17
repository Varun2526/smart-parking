# Contributing to Smart Parking Management System

Thank you for your interest in contributing to the Smart Parking Management System! We welcome contributions from the community.

## 📋 Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Guidelines](#development-guidelines)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)

## 📜 Code of Conduct

This project adheres to a code of conduct that we expect all participants to follow:
- Be respectful and inclusive
- Focus on constructive feedback
- Help maintain a welcoming environment for all contributors

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Git for version control
- Basic understanding of Java and object-oriented programming

### Setting up Development Environment

1. **Fork the repository**
   ```bash
   # Fork the repo on GitHub and clone your fork
   git clone https://github.com/YOUR_USERNAME/smart-parking.git
   cd smart-parking
   ```

2. **Set up upstream remote**
   ```bash
   git remote add upstream https://github.com/Varun2526/smart-parking.git
   ```

3. **Create development branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

4. **Compile and test**
   ```bash
   mkdir -p out
   javac -d out src/**/*.java
   java -cp out ui.cli.MainCLI
   ```

## 🤝 How to Contribute

### Types of Contributions

- **Bug fixes**: Fix issues in existing functionality
- **Feature additions**: Add new features to enhance the system
- **Documentation**: Improve or add documentation
- **Testing**: Add test cases or improve existing tests
- **Performance**: Optimize existing code for better performance

### Areas Looking for Contributions

- Database integration (PostgreSQL/MySQL)
- REST API endpoints
- Web-based interface
- Unit and integration tests
- Performance optimizations
- Mobile app integration
- Documentation improvements

## 💻 Development Guidelines

### Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for all public methods and classes
- Keep methods focused and small (ideally under 30 lines)
- Use proper indentation (4 spaces, no tabs)

### Example Code Style

```java
/**
 * Calculates parking fee based on vehicle type and duration.
 * @param vehicle The parked vehicle
 * @param entryTime When the vehicle was parked
 * @param exitTime When the vehicle exited
 * @return Calculated fee in rupees
 */
public int calculateFee(Vehicle vehicle, LocalDateTime entryTime, LocalDateTime exitTime) {
    if (vehicle == null || entryTime == null || exitTime == null) {
        throw new IllegalArgumentException("Parameters cannot be null");
    }
    
    Duration parkingDuration = Duration.between(entryTime, exitTime);
    long hours = parkingDuration.toHours();
    
    return (int) Math.max(1, hours) * vehicle.getHourlyRate();
}
```

### Architecture Principles

- **Separation of Concerns**: Keep UI, business logic, and data layers separate
- **Single Responsibility**: Each class should have one reason to change
- **Open/Closed Principle**: Open for extension, closed for modification
- **Dependency Injection**: Use constructor injection for dependencies
- **Exception Handling**: Use custom exceptions for business logic errors

### Package Structure

```
src/
├── backend/
│   ├── exceptions/     # Custom exceptions
│   ├── models/         # Data models
│   ├── services/       # Business logic
│   └── utils/          # Utility classes
└── ui/
    ├── cli/           # Command line interface
    └── swing/         # GUI components
```

## 📝 Pull Request Process

1. **Update your fork**
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   ```

2. **Create feature branch**
   ```bash
   git checkout -b feature/descriptive-name
   ```

3. **Make your changes**
   - Write clean, documented code
   - Follow the coding standards
   - Test your changes thoroughly

4. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add descriptive commit message"
   ```

5. **Push to your fork**
   ```bash
   git push origin feature/descriptive-name
   ```

6. **Create Pull Request**
   - Go to GitHub and create a PR from your fork
   - Use descriptive title and detailed description
   - Reference any related issues

### Commit Message Format

Use conventional commit messages:
- `feat:` for new features
- `fix:` for bug fixes
- `docs:` for documentation changes
- `refactor:` for code refactoring
- `test:` for adding tests
- `style:` for formatting changes

Examples:
```
feat: add vehicle reservation system
fix: resolve token validation bug
docs: update API documentation
refactor: extract fee calculation logic
```

## 🐛 Issue Reporting

### Before Creating an Issue

1. **Search existing issues** to avoid duplicates
2. **Test with the latest version** to ensure the bug still exists
3. **Gather information** about your environment

### Creating a Good Issue

**For Bug Reports:**
- Clear, descriptive title
- Steps to reproduce the issue
- Expected vs actual behavior
- Environment details (Java version, OS)
- Relevant code snippets or error messages

**For Feature Requests:**
- Clear description of the proposed feature
- Use cases and benefits
- Possible implementation approach
- Any relevant mockups or diagrams

### Issue Templates

**Bug Report:**
```markdown
## Bug Description
Brief description of the bug

## Steps to Reproduce
1. Step one
2. Step two
3. Expected result vs actual result

## Environment
- Java version:
- Operating System:
- Project version:

## Additional Context
Any other relevant information
```

**Feature Request:**
```markdown
## Feature Description
Clear description of the proposed feature

## Use Case
Why is this feature needed?

## Proposed Implementation
How should this feature work?

## Additional Context
Any mockups, references, or related issues
```

## 🧪 Testing Guidelines

### Writing Tests

- Write unit tests for all public methods
- Include edge cases and error conditions
- Use descriptive test method names
- Follow Arrange-Act-Assert pattern

### Example Test Structure

```java
@Test
public void testCalculateFee_ValidInput_ReturnsCorrectAmount() {
    // Arrange
    Vehicle vehicle = new FourWheeler("TS11AP123");
    LocalDateTime entryTime = LocalDateTime.now().minusHours(2);
    LocalDateTime exitTime = LocalDateTime.now();
    
    // Act
    int fee = feeCalculator.calculateFee(vehicle, entryTime, exitTime);
    
    // Assert
    assertEquals(40, fee); // 2 hours * 20 rupees/hour
}
```

### Running Tests

```bash
# Compile test files (when test framework is added)
javac -cp "out:lib/junit-5.jar" -d out test/**/*.java

# Run tests
java -cp "out:lib/junit-5.jar" org.junit.runner.JUnitCore TestSuite
```

## 📚 Documentation Guidelines

### Code Documentation

- Use JavaDoc for all public classes and methods
- Include parameter descriptions and return values
- Document any exceptions that might be thrown
- Add usage examples for complex methods

### README Updates

- Update relevant sections when adding new features
- Include examples and screenshots when applicable
- Keep installation and usage instructions current
- Update the feature list and architecture diagrams

## 🔄 Review Process

### What We Look For

- **Functionality**: Does the code work as intended?
- **Code Quality**: Is the code clean, readable, and well-structured?
- **Testing**: Are there appropriate tests for new functionality?
- **Documentation**: Is the code properly documented?
- **Performance**: Does the code perform efficiently?

### Review Timeline

- Initial review within 1-2 business days
- Feedback provided on all pull requests
- Multiple review rounds may be necessary
- Final approval and merge when all criteria are met

## 💡 Getting Help

If you need help with your contribution:

1. **Check existing documentation** and issues
2. **Ask questions** in issue comments
3. **Reach out** to maintainers for guidance
4. **Join discussions** in existing issues

## 🎉 Recognition

Contributors will be:
- Listed in the project's contributors section
- Credited in release notes for significant contributions
- Welcomed to join as a maintainer for substantial ongoing contributions

---

Thank you for contributing to the Smart Parking Management System! 🚗✨