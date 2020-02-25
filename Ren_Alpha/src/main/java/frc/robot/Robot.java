/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String _shootingAuton = "Shooting";
  private static final String _ctlAuton = "Cross the Line";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

//Button Values----------------------------------------------------------------------------------------------------------------

  private Integer _upClimbInt = 12;
  private Integer _downClimbInt = 11;

  private Integer _intakeInt = 3;
  private Integer _launchInt = 1;
  private Integer _lowLaunchInt = 5;
  private Integer _reverseInt = 4;

  private Integer _reverseAxisInt = 6; 

  private Integer _wheelSpinnerInt = 7;

  private Integer _limelightInt = 2;

//Encoder Values--------------------------------------------------------------------------------------------------------------------------



//Motor Speeds

  private Double _upClimbMotorSTG1 = 0.7;
  private Double _downClimbMotorSTG1 = 0.8; 
  
  private Double _launcherSpeed = 1.0;
  private Double _lowLauncherSpeed = 0.2;

  private Double _intakeSpeed = 0.42; 
  private Double _intakeRevSpeed = -0.42;
  private Double _topBeltSpeed = 0.9;
  private Double _bottomBeltSpeed = 0.9;
  private Double _topRevBeltSpeed = -0.5;
  private Double _bottomRevBeltSpeed = -0.5;

  private Double _wheelSpinSpeed = 0.1;

  private Double _autonMotorSpeed = 0.2;

//Toggle--------------------------------------------------------------------------------------------------------------------------------------
  
  private Toggle _upClimbTog = new Toggle();
  private Toggle _downClimbTog = new Toggle();

  private Toggle _intakeTog = new Toggle();
  private Toggle _launchTog = new Toggle();
  private Toggle _lowLaunchTog = new Toggle();
  private Toggle _reverseTog = new Toggle();

  private Toggle _reverseAxisTog = new Toggle();

  private Toggle _wheelSpinTog = new Toggle();

  private Toggle _limelightTestTog = new Toggle();

//Drive Train----------------------------------------------------------------------------------------------------------------------------
  
  private WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(3);
  private WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(1);

  private VictorSPX _backRightMotor = new VictorSPX(4);
  private VictorSPX _backLeftMotor = new VictorSPX(2);

  private DifferentialDrive _drive = new DifferentialDrive(_frontRightMotor, _frontLeftMotor);

//Controls-----------------------------------------------------------------------------------------------------------------------------
  
  private Joystick _joystick = new Joystick(0); 

//Launcher-------------------------------------------------------------------------------------------------------------------------------

  private WPI_VictorSPX _intakeMotor = new WPI_VictorSPX(8);

  private WPI_VictorSPX _topBeltMotor = new WPI_VictorSPX(9);
  private WPI_VictorSPX _bottomBeltMotor = new WPI_VictorSPX(12);

  private WPI_VictorSPX _leftLaunchMotor = new WPI_VictorSPX(6);
  private WPI_VictorSPX _rightLaunchMotor = new WPI_VictorSPX(5);

//Limelight--------------------------------------------------------------------------------------------

  private boolean _limelightHasValidTarget = false;
  private double _limelightDriveCommand = 0.0;
  private double _limelightSteerCommand = 0.0;
  
//Climb------------------------------------------------------------------------------------------------------------------
 
  private VictorSPX _upClimbMotor = new VictorSPX(7); 
  private VictorSPX _downClimbMotor = new VictorSPX(10); 

  private DigitalInput _bottomSwitch = new DigitalInput(0);
  private DigitalInput _topSwitch = new DigitalInput(1);

//Color Wheel------------------------------------------------------------------------------------------------------

  private WPI_TalonFX _colorWheelMotor = new WPI_TalonFX (11); 
  
//Auton--------------------------------------------------------------------------------------------------------------------

  private Timer _autonTimer = new Timer();

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

    _backLeftMotor.follow(_frontLeftMotor);
    _backRightMotor.follow(_frontRightMotor);

    _backLeftMotor.setNeutralMode(NeutralMode.Brake);
    _backRightMotor.setNeutralMode(NeutralMode.Brake);
    _frontLeftMotor.setNeutralMode(NeutralMode.Brake);
    _frontRightMotor.setNeutralMode(NeutralMode.Brake);

  //Launcher-----------------------------------------------------------------------------------------

    _rightLaunchMotor.setInverted(true);
    _intakeMotor.setInverted(true);

  //Encoder--------------------------------------------------------------------------------------------

    _frontLeftMotor.setSelectedSensorPosition(0); 

  //Wheel_Spinner----------------------------------------------------------------------------------------

  _colorWheelMotor.setNeutralMode(NeutralMode.Brake);

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

    System.out.println(_frontLeftMotor.getSelectedSensorPosition());

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
    m_autoSelected = SmartDashboard.getString("Auto Selector", _shootingAuton);
    m_autoSelected = SmartDashboard.getString("Auto Selector", _ctlAuton);
    System.out.println("Auto selected: " + m_autoSelected);

    _autonTimer.start();
    _autonTimer.reset();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case _ctlAuton:
      
       if (_autonTimer.get() < 15.0 && _frontLeftMotor.getSelectedSensorPosition() > -1000){

         _drive.arcadeDrive(.25, 0);
         _leftLaunchMotor.set(_launcherSpeed);
         System.out.println("Move back");

       }else{

         _drive.arcadeDrive(0, 0);
        System.out.println("Stop");

       }
        break;

      case _shootingAuton:
      default:
      
      if (_autonTimer.get() < 10.0){

        _leftLaunchMotor.set(_launcherSpeed);
        _rightLaunchMotor.set(_launcherSpeed);
        _topBeltMotor.set(_topBeltSpeed);
        
      
      }else if (_autonTimer.get() > 10.00 && _autonTimer.get() < 15.0){

        _drive.tankDrive(_autonMotorSpeed, _autonMotorSpeed);
      
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

    Update_Limelight_Tracking();

    //Drive_Train-----------------------------------------------------------------------------------------------------------------------

    if(_reverseAxisTog.togglePressed(_joystick, _reverseAxisInt)){

      _drive.arcadeDrive(_joystick.getY(),- _joystick.getX());
    
  }else if(_limelightTestTog.toggleHeld(_joystick, _limelightInt) && _limelightHasValidTarget){

    _drive.arcadeDrive(_limelightDriveCommand, -_limelightSteerCommand);
    _leftLaunchMotor.set(_launcherSpeed);
    _rightLaunchMotor.set(_launcherSpeed);
  
  }else{

      _drive.arcadeDrive(-_joystick.getY(), -_joystick.getX());

    }    

  //Climber----------------------------------------------------------------------------------------------

    if(_upClimbTog.toggleHeld(_joystick, _upClimbInt) && _bottomSwitch.get()){

      _upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSTG1);
    
  }else if(_downClimbTog.toggleHeld(_joystick, _downClimbInt) && _topSwitch.get()){

      _downClimbMotor.set(ControlMode.PercentOutput, _downClimbMotorSTG1);

  }else{

      _downClimbMotor.set(ControlMode.PercentOutput, 0);
      _upClimbMotor.set(ControlMode.PercentOutput, 0);
  
  }
     
  //Launcher----------------------------------------------------------------------------------------------------------------------
    
    if(_intakeTog.toggleHeld(_joystick, _intakeInt)){
      
      _intakeMotor.set(_intakeSpeed);
      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);
      
    
  }else if(_launchTog.toggleHeld(_joystick, _launchInt)){
      
      _leftLaunchMotor.set(_launcherSpeed);
      _rightLaunchMotor.set(_launcherSpeed);
      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);
      _intakeMotor.set(_intakeSpeed);
      

  }else if(_reverseTog.toggleHeld(_joystick, _reverseInt)){
      
      _topBeltMotor.set(_topRevBeltSpeed);
      _bottomBeltMotor.set(_bottomRevBeltSpeed);
      _intakeMotor.set(_intakeRevSpeed);
    
  }else if(_lowLaunchTog.toggleHeld(_joystick, _lowLaunchInt)){

      _leftLaunchMotor.set(_lowLauncherSpeed);
      _rightLaunchMotor.set(_lowLauncherSpeed);
      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);

  }else{
      
      _leftLaunchMotor.set(0);
      _rightLaunchMotor.set(0);
      _topBeltMotor.set(0);
      _intakeMotor.set(0);
      _leftLaunchMotor.setNeutralMode(NeutralMode.Brake);
      _rightLaunchMotor.setNeutralMode(NeutralMode.Brake);
    
    }

  //Wheel spinner---------------------------------------------------------------------------------------------
  
  if(_wheelSpinTog.toggleHeld(_joystick, _wheelSpinnerInt)){
    
    _colorWheelMotor.set(_wheelSpinSpeed);

  }else{

    _colorWheelMotor.set(0);
    
  }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void Update_Limelight_Tracking(){

    final double _steerspeed = 0.26;
    final double _drivespeed = 0.26;
    final double _desired_Target_Area = 1.0; 
    final double _max_drivespeed = 0.7;
    final double _max_steerspeed = 0.7;

    
    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    if(tv < 1.0){

      _limelightHasValidTarget = false;
      _limelightDriveCommand = 0.0;
      _limelightSteerCommand = 0.0;
      return;
    }

    _limelightHasValidTarget = true;

    double _steercmd = tx * _steerspeed;
    _limelightSteerCommand = _steercmd;

      if(_steercmd > _max_steerspeed){

        _steercmd = _max_steerspeed;
      }

    double _drivecmd = (_desired_Target_Area - ta) * _drivespeed;

     if(_drivecmd > _max_drivespeed){

       _drivecmd = _max_drivespeed;
     
      }
     _limelightDriveCommand = _drivecmd;

   }
}
