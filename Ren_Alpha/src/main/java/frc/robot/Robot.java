/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.fasterxml.jackson.core.format.InputAccessor.Std;

import edu.wpi.first.hal.sim.EncoderSim;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import sun.nio.ch.Net;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String _shootingAuton = "Shooting";
  private static final String _ctlAuton = "Cross the line";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Button Values----------------------------------------------------------------------------------------------------------------

  private Integer _transSolenoidInt = 2;

  private Integer _upClimbInt = 12;
  private Integer _downClimbInt = 11;

  private Integer _intakeInt = 3;
  private Integer _launchInt = 1;
  private Integer _lowLaunchInt = 5;
  private Integer _reverseInt = 4;
  private Integer _servoInt = 10;

  private Integer _reverseAxisInt = 6; 

  private Integer _wheelSpinnerInt= 7;

//Encoder Values--------------------------------------------------------------------------------------------------------------------------

  private Integer _centerAutoSTG1 = 500;
  
  private Integer _leftAutoSTG1 = 500;
  private Integer _leftAutoSTG2 = 1000;

  private Integer _rightAutoSTG1 = 500;
  private Integer _rightAutoSTG2 = 1000;

//Motor Speeds

  private Double _upClimbMotorSTG1 = 0.7;
  private Double _upClimbMotorSTG2 = -0.3; 
  private Double _downClimbMotorSTG1 = -0.8; 
  
  private Double _launcherSpeed = 1.0;
  private Double _lowLauncherSpeed = 0.2;

  private Double _intakeSpeed = 0.55; 
  private Double _intakeRevSpeed = -0.55;
  private Double _beltSpeed = 0.9;
  private Double _beltRevSpeed = -0.5;

  private Double _wheelSpinSpeed = 0.5;

//Servo position

  private Double _servoLaunchPos = 0.5;
  private Double _servoNeutralPos = 0.3; 

//Gyro positions

  
  

//Toggle--------------------------------------------------------------------------------------------------------------------------------------
  
  private Toggle _transSolenoidTog = new Toggle();
  
  private Toggle _upClimbTog = new Toggle();
  private Toggle _downClimbTog = new Toggle();

  private Toggle _intakeTog = new Toggle();
  private Toggle _launchTog = new Toggle();
  private Toggle _lowLaunchTog = new Toggle();
  private Toggle _reverseTog = new Toggle();

  private Toggle _servoTog = new Toggle();

  private Toggle _reverseAxisTog = new Toggle();

  private Toggle _wheelSpinTog = new Toggle();

//Drive Train----------------------------------------------------------------------------------------------------------------------------
  
  private VictorSPX _frontRightMotor = new VictorSPX(4);
  private VictorSPX _frontLeftMotor = new VictorSPX(2);

  private WPI_TalonSRX _backRightMotor = new WPI_TalonSRX(3);
  private WPI_TalonSRX _backLeftMotor = new WPI_TalonSRX(1);

  private DifferentialDrive _drive = new DifferentialDrive(_backRightMotor, _backLeftMotor);
//encoder test

Encoder enc;

//Controls-----------------------------------------------------------------------------------------------------------------------------
  
  private Joystick _joystick = new Joystick(0); 

//Transmission

  //private DoubleSolenoid _transSolenoid = new DoubleSolenoid(0, 1);

//Launcher-------------------------------------------------------------------------------------------------------------------------------

  private WPI_VictorSPX _intakeMotor = new WPI_VictorSPX(8);

  private WPI_VictorSPX _beltMotor = new WPI_VictorSPX(9);

  private WPI_VictorSPX _leftLaunchMotor = new WPI_VictorSPX(6);
  private WPI_VictorSPX _rightLaunchMotor = new WPI_VictorSPX(5);
  
  //private Servo _launcherServo = new Servo(0);
  
 
//Climb------------------------------------------------------------------------------------------------------------------
 
  private VictorSPX _upClimbMotor = new VictorSPX(7); 
  private VictorSPX _downClimbMotor = new VictorSPX(10); 

  private DigitalInput _bottomSwitch = new DigitalInput(0);
  private DigitalInput _topSwitch = new DigitalInput(1);

//Color Wheel Motor(Falcon) ------------------------------------------------------------------------------------------------------

private TalonFX _colorWheelMotor = new TalonFX (11);
  
//test--------------------------------------------------------------------------------------------------------------------

  // AHRS _gyro = new AHRS(I2C.Port.kMXP);
  // double heading;
  // double rotateToAngleRate;

  private Timer _autonTimer = new Timer();

  // static final double kP = 1;
  // static final double kI = 0.00f;
  // static final double kD = 0.00f;
  // static final double kF = 0.00f;

  private NetworkTableInstance _limelight;


  

  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Shooting", _shootingAuton);
    m_chooser.addOption("Cross the Line", _ctlAuton);
    SmartDashboard.putData("Auto choices", m_chooser);

  //Drive-------------------------------------------------------------------------------------------------------------

    _frontLeftMotor.follow(_backLeftMotor);
    _frontRightMotor.follow(_backRightMotor);

    _frontLeftMotor.setNeutralMode(NeutralMode.Brake);
    _frontRightMotor.setNeutralMode(NeutralMode.Brake);
    _backLeftMotor.setNeutralMode(NeutralMode.Brake);
    _backRightMotor.setNeutralMode(NeutralMode.Brake);

    


  //Pneumatics--------------------------------------------------------------------------------------------------------------------------------

    //_transSolenoid.set(Value.kReverse);

  //Launcher

    _rightLaunchMotor.setInverted(true);
    _intakeMotor.setInverted(true);

  //Auton test

    //Shuffleboard.getTab("Gyro").add((Sendable) _gyro); 
    //_gyro.getCompassHeading();

    //enc = new Encoder(0, 1);
    //System.out.println(enc.getDistance()); 

    _limelight.getDefault().getTable("limelight").getEntry("<variablename>").setNumber(1);
    float Kp = -0.1f;
    float min_command = 0.05f;
    float tx = table = GetNumber("tx");
    float tx = table = GetNumber("tx"); 
        
    

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    //double dist = enc.getDistance();
    //SmartDashboard.putNumber("Encoder", dist);

    System.out.println(_backLeftMotor.getSelectedSensorPosition());

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", _shootingAuton);
    System.out.println("Auto selected: " + m_autoSelected);

    _autonTimer.start();
    _autonTimer.reset();

    //heading = _gyro.getAngle();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case _ctlAuton:
      
       if (_autonTimer.get() < 15.0){

         _drive.tankDrive(.5, .5);

       }else{

         _drive.arcadeDrive(0, 0);
        
       }
        break;
      case _shootingAuton:
      default:

      //double error = heading - _gyro.getAngle();
      
      if (_autonTimer.get() < 10.0){

        _leftLaunchMotor.set(_launcherSpeed);
        _rightLaunchMotor.set(_launcherSpeed);
        _beltMotor.set(_beltSpeed);
        
      
      }else if (_autonTimer.get() > 10.00 && _autonTimer.get() < 15.0){

        _drive.tankDrive(0.5, 0.5);
      
      }else{

        _drive.tankDrive(0, 0);
      }
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    //Drive_Train-----------------------------------------------------------------------------------------------------------------------

    if(_reverseAxisTog.togglePressed(_joystick, _reverseAxisInt)){

      _drive.arcadeDrive(_joystick.getY(),- _joystick.getX());
    
  }else{

      _drive.arcadeDrive(-_joystick.getY(), -_joystick.getX());

    }    

  //Climber----------------------------------------------------------------------------------------------

    if(_upClimbTog.toggleHeld(_joystick, _upClimbInt) && _bottomSwitch.get()){

      _upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSTG1);
    
  }else if(_downClimbTog.toggleHeld(_joystick, _downClimbInt) && _topSwitch.get()){

      _downClimbMotor.set(ControlMode.PercentOutput, _downClimbMotorSTG1);
      //_upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSTG2);

  }else{

      _downClimbMotor.set(ControlMode.PercentOutput, 0);
      _upClimbMotor.set(ControlMode.PercentOutput, 0);
  
  }

  //Transmission--------------------------------------------------------------------------------------------------
    
    if(_transSolenoidTog.togglePressed(_joystick, _transSolenoidInt)){
      
      //_transSolenoid.set(DoubleSolenoid.Value.kForward);
  
  }else{

      //_transSolenoid.set(DoubleSolenoid.Value.kReverse);
    
    }
  
    
  //Launcher
    
    if(_intakeTog.toggleHeld(_joystick, _intakeInt)){
      
      _intakeMotor.set(_intakeSpeed);
      _beltMotor.set(_beltSpeed);
    
  }else if(_launchTog.toggleHeld(_joystick, _launchInt)){
      
      _leftLaunchMotor.set(_launcherSpeed);
      _rightLaunchMotor.set(_launcherSpeed);
      _beltMotor.set(_beltSpeed);
      _intakeMotor.set(_intakeSpeed);
      

  }else if(_reverseTog.toggleHeld(_joystick, _reverseInt)){
      
      _beltMotor.set(_beltRevSpeed);
      _intakeMotor.set(_intakeRevSpeed);
    
  }else if(_lowLaunchTog.toggleHeld(_joystick, _lowLaunchInt)){

      _leftLaunchMotor.set(_lowLauncherSpeed);
      _rightLaunchMotor.set(_lowLauncherSpeed);
      _beltMotor.set(_beltSpeed);

  }else{
      
      _leftLaunchMotor.set(0);
      _rightLaunchMotor.set(0);
      _beltMotor.set(0);
      _intakeMotor.set(0);
      //_launcherServo.set(0);
      _leftLaunchMotor.setNeutralMode(NeutralMode.Brake);
      _rightLaunchMotor.setNeutralMode(NeutralMode.Brake);
    
    }

  if(_servoTog.toggleHeld(_joystick, _servoInt)){

      //_launcherServo.setPosition(_servoLaunchPos);
  
  }else{

      //_launcherServo.setPosition(_servoNeutralPos);

  }
  
  if(_wheelSpinTog.toggleHeld(_joystick, _wheelSpinnerInt)){
    
    

  }else{
    
    //_colorWheelMotor.set(0);
    
  }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
