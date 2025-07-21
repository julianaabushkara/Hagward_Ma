# Hogwarts Wizardry Program Management System

## Project Overview

A comprehensive **Information System Design** project for managing the **Wizardry Program at Hogwarts School**. This system handles the complete student application and acceptance process, from initial application submission through final admission decisions, including interviews, waiting lists, and conditional acceptances.

## 🎯 System Purpose

The system manages the complex admission process for Hogwarts School's Wizardry Program, handling multiple decision paths and time-sensitive processes while accommodating various stakeholders including applicants, acceptance committees, and the program chair.

## 📋 Core Functionality

### Application Process Management
- **Initial Application Submission**: Handle incoming student applications
- **Multi-Stage Decision Process**: Support various response types (acceptance, rejection, interview, waiting list)
- **Interview Scheduling**: Coordinate interviews with acceptance committee
- **Conditional Acceptance Tracking**: Monitor and evaluate conditional requirements
- **Waiting List Management**: Time-bound waiting periods with automatic processing

### Administrative Features
- **Program Chair Activities**: Support for different operational periods
- **Committee Management**: Handle acceptance committee operations
- **Periodic Reporting**: Generate acceptance reports and statistics
- **Strike Management**: Handle academic staff strikes and process suspension

## 🔄 Application Workflow

### Initial Response Types
1. **✅ Accepted**: Direct admission to the program
2. **❌ Rejected**: Final rejection (cannot reapply)  
3. **📋 Interview Invitation**: Schedule with acceptance committee
4. **⏳ Waiting List**: 3-week maximum waiting period

### Post-Interview Outcomes
- **Acceptance**: Full admission to program
- **Conditional Acceptance**: Admission with specific requirements
- **Rejection**: Final rejection decision
- **Waiting List**: Additional waiting period (max 3 weeks)

### Special Processes
- **Conditional Evaluation**: End-of-semester requirement checking
- **Time-Based Decisions**: Automatic rejection after 3-week waiting periods
- **Strike Handling**: Process suspension and resumption capabilities

## 🏗️ System Architecture
## 🛠️ Development Tools & Technologies
- **Visual Paradigm**: UML model creation (recommended CASE tool)
- **Microsoft Access**: Database implementation and QBE queries
- **Microsoft Office**: Documentation (Word, Excel)
- **LLM Interface**: Mandatory for DRAFT model generation
- **iText Library**: PDF report generation
- **Java Development Environment**: For implementation (if applicable)
- **JSON/XML Libraries**: Data interchange and configuration management
- 
#### UML Models
- **Use Case Diagram**: Complete system functionality overview
- **Class Diagram**: Data entities, attributes, and relationships
- **Sequence Diagrams**: Process flow interactions
- **Statechart Diagram**: Application state transitions 

#### Database Design
- **3NF Tables**: Normalized relational database structure
- **Primary/Foreign Keys**: Proper relationship management
- **QBE Queries**: Periodical acceptance report generation
- **Representative Data**: Comprehensive test data sets

- [ ] **1.1.a**: FINAL UML Use Case Diagram
- [ ] **1.1.b**: FINAL Use Case Descriptions  
- [ ] **1.1.c**: FINAL UML Class Diagram
- [ ] **1.1.d**: 3NF Database Schema (Access)
- [ ] **1.1.e**: QBE "periodical acceptance report" Query

### Quality Standards
- ✅ All models created with CASE tools (Visual Paradigm)
- ✅ Database in 3NF with proper relationships
- ✅ Minimum 20 records per table with edge cases
- ✅ QBE query produces correct report output
- ✅ DRAFT and FINAL models show clear improvements
- ✅ All artifacts properly named and submitted
- 
