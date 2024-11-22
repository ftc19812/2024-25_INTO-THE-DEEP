package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="", group="Linear Opmode")


public class AutonomousSutra extends LinearOpMode {
    // Math for wheel movement
    private final double wheelCircumference = 75*3.14;
    private final double gearReduction = 3.61*5.23;
    private final double counts = 28.0;
    
    private final double rev = counts*gearReduction;
    private final int revPerMM = (int)rev/(int)wheelCircumference;
    private final double inches = revPerMM*25.4;

    // Declare OpMode members for each of the moving parts.

    @Override
    public void runOpMode() {
        
        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.

        // Set Directions
        
        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int count=0;
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()&&count==0) {
            // Gallop on Rocinante!!!
            count++;
        }
    }
    public void input(double leftFront, double rightFront, double leftBack, double rightBack)
    {
        leftFrontDrive.setPower(leftFront);
        rightFrontDrive.setPower(rightFront);
        leftBackDrive.setPower(leftBack);
        rightBackDrive.setPower(rightBack);
        sleep(500);
    }
    public void turnLeft(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(-convert, convert, -convert, convert);
    }
    public void turnRight(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(convert, -convert, convert, -convert);
    }
    public void driveEncoders(int target)
    {
        encoders(target, target, target, target);
    }
    public void backEncoders(int target)
    {
        encoders(-target, -target, -target, -target);
    }
    public void rightEncoders(int target)
    {
        encoders(target, -target, -target, target);
    }
    public void leftEncoders(int target)
    {
        encoders(-target, target, target, -target);
    }
    public void leftTopDiagonal(int target)
    {
        encoders(0, target, target, 0);
    }
    public void rightTopDiagonal(int target)
    {
        encoders(target, 0, 0, target);
    }
    public void leftBottomDiagonal(int target)
    {
        encoders(-target, 0, 0, -target);
    }
    public void rightBottomDiagonal(int target)
    {
        encoders(0, -target, -target, 0);
    }

    // Encoders is a function that makes the four wheels move in a direction by the distance inputted. (100, 100, 0, 0) would make lF and rF go 100mm forwards.
    // The rest of the movement functions rely on this!!!

    public void encoders(int leftFront, int rightFront, int leftBack, int rightBack)
    {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        leftFrontDrive.setTargetPosition(leftFront*revPerMM);
        rightFrontDrive.setTargetPosition(rightFront*revPerMM);
        leftBackDrive.setTargetPosition(leftBack*revPerMM);
        rightBackDrive.setTargetPosition(rightBack*revPerMM);
        
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        double power=0.2;
        leftFrontDrive.setVelocity(1500);
        rightFrontDrive.setVelocity(1500);
        leftBackDrive.setVelocity(1500);
        rightBackDrive.setVelocity(1500);
        while (opModeIsActive()&&leftFrontDrive.isBusy()||rightFrontDrive.isBusy()||leftBackDrive.isBusy()||rightBackDrive.isBusy())
        {
        }
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
    //THIS PART DOWNWARDS ONLY SETS POWER, NOT POSITION
    public void servoPower(double power)
    {
        leftServo.setPower(power);
        rightServo.setPower(power);
    }
    public void armHold()
    {
        rightArm.setPower(-0.1);
        leftArm.setPower(0.1);
    }
    public void armUp(){
        rightArm.setPower(-0.7);
        leftArm.setPower(0.7);
    }
    public void setTheArmPowersToZeroSoTheyFallAndAreStoppedByTheHardstop(){
        rightArm.setPower(0.0);
        leftArm.setPower(0.0);
    }
    public void drive()
    {
        leftFrontDrive.setPower(1.0);
        rightFrontDrive.setPower(1.0);
        leftBackDrive.setPower(1.0);
        rightBackDrive.setPower(1.0);
        //input(0.4, 0.4, 0.4, 0.4);
    }
    public void topRight()
    {
        input(0.4, 0, 0, 0.4);
    }
    public void right()
    {
        input(0.4, -0.4, -0.4, 0.4);
    }
    public void bottomRight()
    {
        input(0, -0.4, -0.4, 0);
    }
    public void back()
    {
        input(-0.4, -0.4, -0.4, -0.4);
    }
    public void bottomLeft()
    {
        input(-0.4, 0, 0, -0.4);
    }
    public void left()
    {
        input(-0.4, 0.4, 0.4, -0.4);   
    }
    public void topLeft()
    {
        input(0, 0.4, 0.4, 0);
    }
    public void stop(double time)
    {
        input(0, 0, 0, 0);
    }
      public void basket(){
        driveEncoders(300);
        turnLeft(90);
        driveEncoders(1220);
        turnLeft(45);
        // arm to score thing here
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //idk sam said to put it
        slideMotor.setTargetPosition(-3100.0); // arm up to basket
        slideMotor.setVelocity(1500); //idk sam said to put it
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (slideMotor.isBusy()){
        }

        intakePivotMotor.setPower(-0.5);
        sleep(400);
        intakePivotMotor.setPower(0.3); //anti-grav power


        upperServo.setPower(1.0);
        lowerServo.setPower(-1.0);

        sleep(3000);
        intakePivotMotor.setPower(0.7);

        //this is where the scoring thing stops
        backEncoders(300);
        slideMotor.setTargetPosition(0.0); // arm down from basket
        slideMotor.setVelocity(1500); //idk sam said to put it
        turnRight(135);
        rightEncoders(3050);
        backEncoders(610);
    }
    }
    public void observation(){
        driveEncoders(300);
        turnLeft(90);
        driveEncoders(1830);
        turnLeft(45);
        // arm to score thing here
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //idk sam said to put it
        slideMotor.setTargetPosition(-3100.0); // arm up to basket
        slideMotor.setVelocity(1500); //idk sam said to put it
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (slideMotor.isBusy()){
        }

        intakePivotMotor.setPower(-0.5);
        sleep(400);
        intakePivotMotor.setPower(0.3); //anti-grav power


        upperServo.setPower(1.0);
        lowerServo.setPower(-1.0);

        sleep(3000);
        intakePivotMotor.setPower(0.7);

        //this is where the scoring thing stops
        backEncoders(300);
        slideMotor.setTargetPosition(0.0); // arm down from basket
        slideMotor.setVelocity(1500); //idk sam said to put it
        turnRight(135);
        rightEncoders(3050);
        backEncoders(610);
    }
}
