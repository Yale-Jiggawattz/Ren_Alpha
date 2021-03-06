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
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer; 
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //private static final String _shootingAuton = "Shooting";
  private static final String _highShootAuto = "High Shoot Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

//Button Values----------------------------------------------------------------------------------------------------------------

  private Integer _upClimbInt = 12;
  private Integer _downClimbInt = 11;

  private Integer _intakeInt = 3;
  private Integer _launchInt = 1;
  private Integer _beltInt = 4;
  private Integer _lowLaunchInt = 11;
  private Integer _reverseInt = 5;

  private Integer _reverseAxisInt = 2; 

  private Integer _wheelSpinnerInt = 2;

  private Integer _limelightInt = 6;
  private Integer _limelightPowerInt = 9;

//Encoder Values--------------------------------------------------------------------------------------------------------------------------

//Color Wheel Values

private final Color _blueVal = ColorMatch.makeColor(0.143, 0.427, 0.429);
private final Color _greenVal = ColorMatch.makeColor(0.197, 0.561, 0.240);
private final Color _redVal = ColorMatch.makeColor(0.561, 0.232, 0.114);
private final Color _yellowVal = ColorMatch.makeColor(0.361, 0.524, 0.113);
<<<<<<< HEAD

public double _numberofSpins;
public double _numberofYellow;
=======
>>>>>>> parent of 17807a3... Ready for competition

//Motor Speeds

  private Double _upClimbMotorSpeed = 0.8;
  private Double _downClimbMotorSpeed = -0.8; 
  
  private Double _launcherSpeed = -0.75;
  private Double _lowLauncherSpeed = -0.15;

  private Double _intakeSpeed = 0.6; 
  private Double _intakeRevSpeed = -0.6;
  private Double _topBeltSpeed = 0.75;
  private Double _bottomBeltSpeed = -0.6;
  private Double _topRevBeltSpeed = -0.5;
  private Double _bottomRevBeltSpeed = -0.5;

  private Double _wheelSpinSpeed = 0.15;
  private Double _wheelSpinDriveSpeed = 0.2;

  private Double _autonDriveSpeed = -0.45;

//Toggle--------------------------------------------------------------------------------------------------------------------------------------
  
  private Toggle _upClimbTog = new Toggle();
  private Toggle _downClimbTog = new Toggle();

  private Toggle _intakeTog = new Toggle();
  private Toggle _launchTog = new Toggle();
  private Toggle _lowLaunchTog = new Toggle();
  private Toggle _reverseTog = new Toggle();
  private Toggle _beltTog = new Toggle();

  private Toggle _reverseAxisTog = new Toggle();

  private Toggle _wheelSpinTog = new Toggle();

  private Toggle _limelightTestTog = new Toggle();
  private Toggle _limelightPowerTog = new Toggle();

//Drive Train----------------------------------------------------------------------------------------------------------------------------
  
  private WPI_TalonSRX _frontRightMotor = new WPI_TalonSRX(3);
  private WPI_TalonSRX _frontLeftMotor = new WPI_TalonSRX(1);

  private VictorSPX _backRightMotor = new VictorSPX(4);
  private VictorSPX _backLeftMotor = new VictorSPX(2);

  private DifferentialDrive _drive = new DifferentialDrive(_frontRightMotor, _frontLeftMotor);

//Controls-----------------------------------------------------------------------------------------------------------------------------
  
  private Joystick _joystick1 = new Joystick(0); 
  private Joystick _joystick2 = new Joystick(1);

//Launcher-------------------------------------------------------------------------------------------------------------------------------

  private WPI_VictorSPX _intakeMotor = new WPI_VictorSPX(8);

  private WPI_VictorSPX _topBeltMotor = new WPI_VictorSPX(9);
  private WPI_VictorSPX _bottomBeltMotor = new WPI_VictorSPX(12);

  private WPI_VictorSPX _leftLaunchMotor = new WPI_VictorSPX(6);
  private WPI_VictorSPX _rightLaunchMotor = new WPI_VictorSPX(5);

  private DigitalInput _beltSwitch = new DigitalInput(2);

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

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 _colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch _colorMatcher = new ColorMatch();
  
  
//Auton--------------------------------------------------------------------------------------------------------------------

  private Timer _autonTimer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    //m_chooser.setDefaultOption("Shooting", _shootingAuton);  
    m_chooser.addOption("Cross the Line", _highShootAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

  //Drive-------------------------------------------------------------------------------------------------------------

    _backLeftMotor.follow(_frontLeftMotor);
    _backRightMotor.follow(_frontRightMotor);

    // _backLeftMotor.setNeutralMode(NeutralMode.Brake);
    // _backRightMotor.setNeutralMode(NeutralMode.Brake);
    // _frontLeftMotor.setNeutralMode(NeutralMode.Brake);
    // _frontRightMotor.setNeutralMode(NeutralMode.Brake);

  //Launcher-----------------------------------------------------------------------------------------

    _rightLaunchMotor.setInverted(true);
    _intakeMotor.setInverted(true);

  //Encoder--------------------------------------------------------------------------------------------

    _frontLeftMotor.setSelectedSensorPosition(0); 

  //Wheel_Spinner----------------------------------------------------------------------------------------

    _colorWheelMotor.setNeutralMode(NeutralMode.Brake);

    _colorMatcher.addColorMatch(_blueVal);
    _colorMatcher.addColorMatch(_greenVal);
    _colorMatcher.addColorMatch(_redVal);
    _colorMatcher.addColorMatch(_yellowVal);

    _numberofYellow = 0;
    _numberofSpins = 0;

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

    //System.out.println(_frontLeftMotor.getSelectedSensorPosition());

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
    //m_autoSelected = SmartDashboard.getString("Auto Selector", _shootingAuton);
    m_autoSelected = SmartDashboard.getString("Auto Selector", _highShootAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    _autonTimer.start();
    _autonTimer.reset();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

    Update_Limelight_Tracking();

    switch (m_autoSelected) {
      case _highShootAuto:
      default:
      
       if (_autonTimer.get() < 5.0){

         _drive.arcadeDrive(_limelightDriveCommand, -_limelightSteerCommand);
         _leftLaunchMotor.set(0);
         _rightLaunchMotor.set(0);
         _topBeltMotor.set(0);
         _bottomBeltMotor.set(0);
         _intakeMotor.set(0);
         NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);

       }else if(_autonTimer.get() > 5.0 && _autonTimer.get() < 10.0){

        _drive.arcadeDrive(0, 0);
        _leftLaunchMotor.set(_launcherSpeed);
        _rightLaunchMotor.set(_launcherSpeed);
        _topBeltMotor.set(_topBeltSpeed);
        _bottomBeltMotor.set(_bottomBeltSpeed);
        _intakeMotor.set(_intakeSpeed);NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);

       }else if(_autonTimer.get() > 10.0 && _autonTimer.get() < 15.0){

        _drive.arcadeDrive(_autonDriveSpeed, 0);
        _leftLaunchMotor.set(0);
        _rightLaunchMotor.set(0);
        _topBeltMotor.set(0);
        _bottomBeltMotor.set(0);
        _intakeMotor.set(0);NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);

       }else{

        _drive.arcadeDrive(0, 0);
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
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

    //  if(_reverseAxisTog.togglePressed(_joystick2, _reverseAxisInt)){

    //     _drive.arcadeDrive(_joystick2.getY(),- _joystick2.getX());
    //     NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
    
    // }else if(_limelightTestTog.toggleHeld(_joystick1, _limelightInt) && _limelightHasValidTarget){

    //   _drive.arcadeDrive(-_joystick2.getY(), -_limelightSteerCommand);
    //   NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
  
    // }else{

    //    _drive.arcadeDrive(-_joystick2.getY(), -_joystick2.getX());
    //    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);

    //   }    

  _drivetrainFunctions();

  //Climber----------------------------------------------------------------------------------------------

    if(_upClimbTog.toggleHeld(_joystick2, _upClimbInt) && _bottomSwitch.get()){

      _upClimbMotor.set(ControlMode.PercentOutput, _upClimbMotorSpeed);
    
  }else if(_downClimbTog.toggleHeld(_joystick2, _downClimbInt) && _topSwitch.get()){

      _downClimbMotor.set(ControlMode.PercentOutput, _downClimbMotorSpeed);

  }else{

      _downClimbMotor.set(ControlMode.PercentOutput, 0);
      _upClimbMotor.set(ControlMode.PercentOutput, 0);
  
  }
     
  //Launcher----------------------------------------------------------------------------------------------------------------------
    
    if(_intakeTog.toggleHeld(_joystick1, _intakeInt) && _beltSwitch.get()){
      
      _intakeMotor.set(_intakeSpeed);
      
  }if(_launchTog.toggleHeld(_joystick1, _launchInt)){
      
      _leftLaunchMotor.set(_launcherSpeed);
      _rightLaunchMotor.set(_launcherSpeed);
      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);

  }if(_reverseTog.toggleHeld(_joystick1, _reverseInt)){
      
      _topBeltMotor.set(_topRevBeltSpeed);
      _bottomBeltMotor.set(-_bottomRevBeltSpeed);
      _intakeMotor.set(_intakeRevSpeed);
      
    
  }if(_lowLaunchTog.toggleHeld(_joystick1, _lowLaunchInt)){

      _leftLaunchMotor.set(_lowLauncherSpeed);
      _rightLaunchMotor.set(_lowLauncherSpeed);
      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);

  }if(_beltTog.toggleHeld(_joystick1, _beltInt)){

      _topBeltMotor.set(_topBeltSpeed);
      _bottomBeltMotor.set(_bottomBeltSpeed);
        
  }if(!_launchTog.toggleHeld(_joystick1, _launchInt)){
      
      _leftLaunchMotor.set(0);
      _rightLaunchMotor.set(0);
      _leftLaunchMotor.setNeutralMode(NeutralMode.Brake);
      _rightLaunchMotor.setNeutralMode(NeutralMode.Brake);

  }if(!_launchTog.toggleHeld(_joystick1, _launchInt) && !_reverseTog.toggleHeld(_joystick1, _reverseInt) && !_lowLaunchTog.toggleHeld(_joystick1, _lowLaunchInt) && !_beltTog.toggleHeld(_joystick1, _beltInt)){
      _topBeltMotor.set(0);
      _bottomBeltMotor.set(0);
      
  }if((!_intakeTog.toggleHeld(_joystick1, _intakeInt) && !_reverseTog.toggleHeld(_joystick1, _reverseInt)) || !_beltSwitch.get()){
      _intakeMotor.set(0);
  
  }

  //Color Sensor-------------------------------------------------------------------------------------------------------------------------

  Color _detectedColor = _colorSensor.getColor();

  String _colorString;
  ColorMatchResult _colorSensorResult = _colorMatcher.matchClosestColor(_detectedColor);

  if(_colorSensorResult.color == _blueVal){

    _colorString = "Blue";

  }else if(_colorSensorResult.color == _redVal){

    _colorString = "Red";

  }else if(_colorSensorResult.color == _greenVal){

    _colorString = "Green";

  }else if (_colorSensorResult.color == _yellowVal){

    _colorString = "Yellow";
    

  }else{

    _colorString = "Unkown or No Color";
  }

  SmartDashboard.putNumber("Red", _detectedColor.red);
  SmartDashboard.putNumber("Green", _detectedColor.green);
  SmartDashboard.putNumber("Blue", _detectedColor.blue);
  SmartDashboard.putNumber("Confidence", _colorSensorResult.confidence);
  SmartDashboard.putString("Detected Color", _colorString);

  String _gameData;
  
  _gameData = DriverStation.getInstance().getGameSpecificMessage();
  SmartDashboard.putString("Field Color", _gameData);

  //Wheel spinner---------------------------------------------------------------------------------------------
  
  // if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && DriverStation.getInstance().getGameSpecificMessage() == "Blue" && _colorString != "Blue"){
    
  //   _colorWheelMotor.set(_wheelSpinSpeed);

  // }else if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && DriverStation.getInstance().getGameSpecificMessage() == "Red" && _colorString != "Red"){

  //   _colorWheelMotor.set(_wheelSpinSpeed);
    
  // }else if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && DriverStation.getInstance().getGameSpecificMessage() == "Green" && _colorString != "Green"){

  //   _colorWheelMotor.set(_wheelSpinSpeed);

  // }else if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && DriverStation.getInstance().getGameSpecificMessage() == "Yellow" && _colorString != "Yellow"){

  //   _colorWheelMotor.set(_wheelSpinSpeed);

  // }else{

  //   _colorWheelMotor.set(0);

  // }

   if(_gameData.length() > 0){

     switch (_gameData.charAt(0)){

       case 'B' :

       if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) &&  _colorSensorResult.color != _blueVal){
    
        _colorWheelMotor.set(_wheelSpinSpeed);
<<<<<<< HEAD
<<<<<<< HEAD
        _drive.arcadeDrive(_wheelSpinDriveSpeed, 0);

       }else{

        _colorWheelMotor.set(0);
        _drivetrainFunctions();
=======
        _colorString = "Blue";

>>>>>>> parent of 17807a3... Ready for competition
=======
        _colorString = "Blue";

>>>>>>> parent of 17807a3... Ready for competition
       }
       break;

       case 'G' :

       if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && _colorSensorResult.color != _greenVal){
    
        _colorWheelMotor.set(_wheelSpinSpeed);
        _drive.arcadeDrive(_wheelSpinDriveSpeed, 0);

<<<<<<< HEAD
<<<<<<< HEAD
       }else{

        _colorWheelMotor.set(0);
        _drivetrainFunctions();
=======
>>>>>>> parent of 17807a3... Ready for competition
=======
>>>>>>> parent of 17807a3... Ready for competition
       }
       break;

       case 'R' :

       if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && _colorSensorResult.color != _redVal){
    
        _colorWheelMotor.set(_wheelSpinSpeed);
        _drive.arcadeDrive(_wheelSpinDriveSpeed, 0);

<<<<<<< HEAD
<<<<<<< HEAD
       }else{

        _colorWheelMotor.set(0);
        _drivetrainFunctions();
=======
>>>>>>> parent of 17807a3... Ready for competition
=======
>>>>>>> parent of 17807a3... Ready for competition
       }
       break;

       case 'Y' :

       if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt) && _colorSensorResult.color != _yellowVal){
    
        _colorWheelMotor.set(_wheelSpinSpeed);
        _drive.arcadeDrive(_wheelSpinDriveSpeed, 0);

<<<<<<< HEAD
<<<<<<< HEAD
       }else{

        _colorWheelMotor.set(0);
        _drivetrainFunctions();
=======
>>>>>>> parent of 17807a3... Ready for competition
=======
>>>>>>> parent of 17807a3... Ready for competition
       }
       break;

       default :

<<<<<<< HEAD
<<<<<<< HEAD
       if(_wheelSpinTog.toggleHeld(_joystick1, _wheelSpinnerInt)){
        
        _colorWheelMotor.set(_wheelSpinSpeed);
        _drive.arcadeDrive(_wheelSpinDriveSpeed, 0);

       }else{

       _colorWheelMotor.set(0);
       _drivetrainFunctions();
       }

      //  if(_colorSensorResult.color == _yellowVal){

      //   _numberofYellow = 1;
        
      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 1){

      //   _numberofYellow = 2;
      //   _numberofSpins = 1;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 2){

      //   _numberofYellow = 3;
        
      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 3){

      //   _numberofYellow = 4;
      //   _numberofSpins = 2;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 4){

      //   _numberofYellow = 5;
       
      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 5){

      //   _numberofYellow = 6;
      //   _numberofSpins = 3;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 6){

      //   _numberofYellow = 7;
        
      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 7){

      //   _numberofYellow = 8;
      //   _numberofSpins = 4;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 8){

      //   _numberofYellow = 9;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofYellow == 9){

      //   _numberofYellow = 10;
      //   _numberofSpins = 5;

      //  }else if(_colorSensorResult.color == _yellowVal && _numberofSpins == 5){

      //   _numberofYellow = 0;
      //   _numberofSpins = 0;

      //  }else{

      //   _numberofYellow = 0;
      //   _numberofSpins = 0;
      //  }
        
=======
       _colorWheelMotor.set(0);

>>>>>>> parent of 17807a3... Ready for competition
=======
       _colorWheelMotor.set(0);

>>>>>>> parent of 17807a3... Ready for competition
       break;
     }
   }else{

  }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public void Update_Limelight_Tracking(){

    final double _steerspeed = 0.4;
    final double _drivespeed = 0.25;
    final double _desired_Target_Area = 1.0; 
    final double _max_drivespeed = 0.55;
    final double _max_steerspeed = 0.65;
    
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

      if(_steercmd > 0 && _steercmd > _max_steerspeed){

        _steercmd = _max_steerspeed;

      }else if(_steercmd < 0 && _steercmd < -_max_steerspeed){

        _steercmd = -_max_steerspeed;

      }

      _limelightSteerCommand = _steercmd;
      //System.out.println("Lime_Cmd= " + _limelightSteerCommand);
      //System.out.println("Lime_Tgt= " + _limelightHasValidTarget);
 

    double _drivecmd = (_desired_Target_Area - ta) * _drivespeed;

     if(_drivecmd > _max_drivespeed){

       _drivecmd = _max_drivespeed;
     
      }
     _limelightDriveCommand = _drivecmd;

   }
  
  public void _drivetrainFunctions(){

    if(_reverseAxisTog.togglePressed(_joystick1, _reverseAxisInt)){

      _drive.arcadeDrive(_joystick1.getY(),- _joystick1.getX());
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
   
  }else if(_limelightTestTog.toggleHeld(_joystick1, _limelightInt) && _limelightHasValidTarget){

    _drive.arcadeDrive(-_joystick1.getY(), -_limelightSteerCommand);
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
 
  }else{

     _drive.arcadeDrive(-_joystick1.getY(), -_joystick1.getX());
     NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);

    }

    // if(_limelightPowerTog.togglePressed(_joystick1, _limelightPowerInt)){

    //   NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);

    // }else{

    //   NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0);
    // }
  }
}
