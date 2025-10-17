# Smart Parking Management System 🚗🏢
A comprehensive parking management solution built in Java that provides both CLI and GUI interfaces for efficient parking operations. The system manages multi-floor parking facilities with support for different vehicle types and automated fee calculation.

## 🌟 Features

### Core Functionality
- **Multi-Vehicle Support**: Two-wheelers, Four-wheelers, and Heavy vehicles
- **Multi-Floor Management**: Ground floor (G1) and First floor (F1) with expandable architecture
- **Automated Slot Allocation**: Smart allocation based on vehicle type and availability
- **Token-Based System**: Secure token generation for vehicle tracking
- **Real-Time Fee Calculation**: Dynamic pricing based on vehicle type and parking duration
- **Vehicle Search**: Quick lookup by registration number
- **Persistent Storage**: Token data persistence across sessions

### User Interfaces
- **Command Line Interface (CLI)**: Terminal-based interaction for quick operations
- **Graphical User Interface (GUI)**: Swing-based visual interface with real-time slot visualization


## 🏗️ System Architecture

```
Smart Parking System
├── Backend Services
│   ├── Models (Vehicle, ParkingSlot, Token, etc.)
│   ├── Services (ParkingService, FeeCalculator, SlotAllocator)
│   ├── Exceptions (Custom exception handling)
│   └── Utilities (TokenStorage, DisplayHelper)
└── User Interfaces
    ├── CLI (Command Line Interface)
    └── GUI (Swing-based Graphical Interface)
```

### Vehicle Types & Pricing
| Vehicle Type | Slots Available | Hourly Rate |
|-------------|----------------|-------------|
| Two Wheeler | G1-TW-1 to G1-TW-5, F1-TW-1 to F1-TW-5 | ₹10/hour |
| Four Wheeler | G1-FW-6 to G1-FW-12, F1-FW-6 to F1-FW-12 | ₹20/hour |
| Heavy Vehicle | G1-HV-13 to G1-HV-15, F1-HV-13 to F1-HV-15 | ₹50/hour |

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Terminal/Command Prompt access

### Installation

1. **Clone the repository**
   
   git clone https://github.com/Varun2526/smart-parking.git
   cd smart-parking
   

2. **Compile the project**
   
   # Create output directory
   mkdir -p out
   
   # Compile all Java files
   javac -d out src/**/*.java
   

### Running the Application

#### Option 1: Command Line Interface (CLI)
```bash
# Run from project root directory
java -cp out ui.cli.MainCLI
```

#### Option 2: Graphical User Interface (GUI)
```bash
# Run from project root directory
java -cp out ui.swing.ParkingFrame
```

## 📋 Usage Guide

### CLI Interface Operations

1. **Park a Vehicle**
   - Enter registration number (e.g., TS11AP456)
   - Select vehicle type (1=Two Wheeler, 2=Four Wheeler, 3=Heavy Vehicle)
   - Receive token ID and slot allocation

2. **Exit Vehicle**
   - Enter the token ID provided during parking
   - System calculates and displays parking fee

3. **Search Vehicle**
   - Enter registration number to find parking slot

4. **View Available Slots**
   - Displays real-time grid of all parking slots

5. **Parking Summary**
   - Shows occupancy statistics for all floors

### GUI Interface Features

- **Visual Slot Grid**: Real-time visualization of parking slots
- **Color-Coded Status**: 
  - 🟢 Green: Available slots
  - 🔴 Red: Occupied slots
  - Different shades for different vehicle types
- **Control Panel**: Easy-to-use buttons for all operations
- **Automatic Refresh**: UI updates automatically after operations

## 🏗️ Project Structure

```
src/
├── backend/
│   ├── exceptions/
│   │   ├── InvalidTokenException.java
│   │   ├── SlotNotAvailableException.java
│   │   └── VehicleNotFoundException.java
│   ├── models/
│   │   ├── Vehicle.java (Abstract base class)
│   │   ├── TwoWheeler.java
│   │   ├── FourWheeler.java
│   │   ├── HeavyVehicle.java
│   │   ├── ParkingSlot.java
│   │   ├── ParkingFloor.java
│   │   └── Token.java
│   ├── services/
│   │   ├── ParkingService.java (Core business logic)
│   │   ├── FeeCalculator.java
│   │   └── SlotAllocator.java
│   └── utils/
│       ├── TokenStorage.java
│       ├── TokenGenerator.java
│       └── DisplayHelper.java
└── ui/
    ├── cli/
    │   └── MainCLI.java
    └── swing/
        ├── ParkingFrame.java
        └── components/
            ├── ControlPanel.java
            └── SlotButton.java
```

## 🎯 Key Classes

- **ParkingService**: Main service class handling all parking operations
- **Vehicle**: Abstract base class with concrete implementations for different vehicle types
- **ParkingSlot**: Represents individual parking spaces with type compatibility
- **Token**: Manages parking tokens with entry/exit timestamps
- **FeeCalculator**: Handles dynamic fee calculation based on vehicle type and duration

## 💾 Data Persistence

The system uses file-based storage (`tokens.txt`) to persist parking data across sessions. Token information includes:
- Token ID
- Slot allocation
- Vehicle registration number
- Entry timestamp

## 🛡️ Error Handling

- **InvalidTokenException**: Thrown for invalid or expired tokens
- **SlotNotAvailableException**: Thrown when no suitable parking slots are available
- **VehicleNotFoundException**: Thrown when searching for non-existent vehicles
- **Input Validation**: Comprehensive validation for registration numbers and user inputs

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🔮 Future Enhancements

- [ ] Database integration (PostgreSQL/MySQL)
- [ ] REST API endpoints
- [ ] Web-based interface
- [ ] Mobile app integration
- [ ] Payment gateway integration
- [ ] Reservation system
- [ ] Analytics dashboard
- [ ] Multi-language support

## 📞 Support

If you encounter any issues or have questions, please create an issue on GitHub or contact the maintainer.

---

**Happy Parking! 🚗✨**
