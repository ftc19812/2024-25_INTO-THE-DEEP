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

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;
    private DcMotor slideArm = null;
    private DcMotor intakePivotMotor = null;
    private CRServo intakeServo = null;

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

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "frontLeft");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "backLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");
        slideArm = hardwareMap.get(DcMotor.class, "slideMotor");
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        intakePivotMotor = hardwareMap.get(DcMotor.class, "intakePivotMotor");

        // Set Directions
        
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        
        slideArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);

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

    public void linearSlideEncoders(int goalPos)
    {
        slideArm.setTargetPosition(goalPos);
        slideArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()&&slideArm.isBusy())
        {
        }

        slideArm.setPower(0);
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

    public void intakePivotEncoders(String state)
    {
        if(state == "Up"){
            intakePivotMotor.setPower(0.4);
        } else if (state == "Down"){
            intakePivotMotor.setPower(-0.55);
        } else if (state == "Hold"){
            intakePivotMotor.setPower(0.2);
        }
    }

    //sam this is the part you read
    //vvvvvvvvvvvvvvvvvvvvvvvvvvvv


        public void basket()
    {
        leftEncoders(1220);
        turnLeft(135);

        //score start :o
        linearSlideEncoders(-3000);
        while(slideArm.isBusy)
        {
        }
        intakePivotEncoders("Up");
        sleep(700);
        intakePivotEncoders("Hold");
        intakeEncoders("Out");
        sleep(500);
        intakeEncoders("Stop");
        linearSlideEncoders(0);
        while(slideArm.isBusy)
        {
        }
        //score end :)

        

        intakeServo.setPower(-1.0);
        sleep(5000);
        intakeServo.setPower(0.0);
        //score end
        turnRight(135);
        rightEncoders(3050);
        backEncoders(305);
    }

    public void observation()
    {
        leftEncoders(1830);
        turnLeft(135);

        //score start :o
        linearSlideEncoders(-3000);
        while(slideArm.isBusy)
        {
        }
        intakePivotEncoders("Up");
        sleep(700);
        intakePivotEncoders("Hold");
        intakeEncoders("Out");
        sleep(500);
        intakeEncoders("Stop");
        linearSlideEncoders(0);
        while(slideArm.isBusy)
        {
        }
        //score end :)
        


        intakeServo.setPower(-1.0);
        sleep(5000);
        intakeServo.setPower(0.0);
        //score end
        turnRight(135);
        rightEncoders(3050);
        backEncoders(305);
    }
}
