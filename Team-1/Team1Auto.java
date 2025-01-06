
package RobotCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Team 1 Autonomous", group="Linear Opmode")


public class Team1Auto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;
    private DcMotorEx slideMotor = null;
    private DcMotorEx intakePivotMotor = null;
    private CRServo intakeServo = null;
    private int clawState = 1;
    private double slideMotorCD = 0.0;
    private double clawCD = 0.0;
    private double motorPosition = 0.0;
    private double pivotIntakeMotorPower = 0.0;
    private boolean slowMode = false;
    private double slowModeCD = 0.0;

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
        
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        leftBackDrive  = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "frontRight");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "backRight");
        slideMotor = hardwareMap.get(DcMotorEx.class, "slideMotor");
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        intakePivotMotor = hardwareMap.get(DcMotorEx.class, "intakePivotMotor");

        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        waitForStart();
        runtime.reset();

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
            basket();
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
    public void turnRight(int angle)
    {
        int convert=revPerMM*angle*(38/10)+(angle*12/10);
        encoders(-convert, convert, -convert, convert);
    }
    public void turnLeft(int angle)
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
    public void leftEncoders(int target)
    {
        encoders(target, -target, -target, target);
    }
    public void rightEncoders(int target)
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
    public void linearSlideEncoders(int goalPos)
    {
        slideMotor.setTargetPosition(goalPos);
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        double power=0.2;
        
        slideMotor.setVelocity(1500);
        
        
        while (opModeIsActive()&&slideMotor.isBusy())
        {
        }

        slideMotor.setPower(0);
    }

    public void intakeEncoders(String state)
    {
        if(state == "In"){
            intakeServo.setPower(-1.0);
        } else if(state == "Out"){
            intakeServo.setPower(1.0);
        } else if(state == "Stop"){
            intakeServo.setPower(0.0);
        }
    }

    public void intakePivotEncoders(String states)
    {
        if(states == "Up"){
            intakePivotMotor.setPower(0.4);
        } else if (states == "Down"){
            intakePivotMotor.setPower(-0.5);
        } else if (states == "Hold"){
            intakePivotMotor.setPower(0.2);
        }
    }
    
    public void basket()
    {
        intakePivotEncoders("Up");
        sleep(400);
        intakePivotMotor.setPower(0.0);
        
        driveEncoders(305);
        
        leftEncoders(1220);
        turnLeft(135);

        //score start :o
        linearSlideEncoders(-3000);
        slideMotor.setPower(-0.15);
        intakePivotEncoders("Down");
        sleep(500);
        intakePivotEncoders("Hold");
        intakeEncoders("Out");
        sleep(3000);
        intakeEncoders("Stop");
        
        intakePivotEncoders("Up");
        sleep(3000);
        backEncoders(500);
        slideMotor.setPower(0.0);
        linearSlideEncoders(0);
        //score end :)

        // turnRight(135);
        // rightEncoders(3050);
        // backEncoders(405);
    }

    public void observation()
    {
        intakePivotEncoders("Up");
        sleep(400);
        intakePivotMotor.setPower(0.0);
        
        driveEncoders(300);
        leftEncoders(1830);
        turnLeft(135);

        //score start :o
        linearSlideEncoders(-3000);
        slideMotor.setPower(-0.15);

        intakePivotEncoders("Down");
        sleep(500);
        intakePivotEncoders("Hold");
        intakeEncoders("Out");
        sleep(3000);
        intakeEncoders("Stop");
        
        intakePivotEncoders("Up");
        sleep(3000);
        backEncoders(500);
        slideMotor.setPower(0.0);
        linearSlideEncoders(0);
        
        //score end :)
        
        //score end
        // turnRight(135);
        // rightEncoders(3050);
        // backEncoders(405);
    }
}


// for pull request main
