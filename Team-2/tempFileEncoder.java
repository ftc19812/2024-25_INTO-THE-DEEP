import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Search up "Java Enums" for an explanation of this topic. I'll cover it in the next few meetings.

enum LinearSlideStates2 {

    SlideUp {
        @Override
        public LinearSlideStates2 nextState() { //Settings a function for the states to use each. Return = when function is called, set value to this state.
            return HoldingOne;
        }
    },
    HoldingOne {
        @Override
        public LinearSlideStates2 nextState() {
            return SlideDown;
        }
    },
    SlideDown {
        @Override
        public LinearSlideStates2 nextState() {
            return HoldingTwo;
        }
    },
    HoldingTwo {
        @Override
        public LinearSlideStates2 nextState() {
            return SlideUp;
        }
    };

    public abstract LinearSlideStates2 nextState(); 
}


@TeleOp(name="Linear Slide Test 2 but actually 1", group="Linear OpMode")

public class LinearSlideTestTeam2 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor slideArm = null;
    private DcMotor intakePivotMotor = null;
    private CRServo intakeServo = null;
    private int clawState = 1;
    private double slideArmCD = 0.0;
    private double clawCD = 0.0;
    LinearSlideStates2 slideArmState = LinearSlideStates2.HoldingTwo;
    private double motorPosition = 0.0;
    // private double pivotIntakeMotorPower = 0.0;
    private boolean slowMode = false;
    private double slowModeCD = 0.0;
    private double pivotPosition = 0.0;
    @Override
    public void runOpMode() {

        leftFrontDrive  = hardwareMap.get(DcMotor.class, "frontLeft");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "backLeft");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "frontRight");
        rightBackDrive = hardwareMap.get(DcMotor.class, "backRight");
        slideArm = hardwareMap.get(DcMotor.class, "slideMotor");
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        intakePivotMotor = hardwareMap.get(DcMotor.class, "intakePivotMotor");

        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        slideArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        intakePivotMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakePivotMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double max;
            motorPosition = slideArm.getCurrentPosition();
            pivotPosition = intakePivotMotor.getCurrentPosition();
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  -gamepad1.left_stick_x;
            double yaw     =  -gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            if(gamepad1.b && runtime.time()-slideArmCD >= 0.2){
                slideArmState = slideArmState.nextState();
                slideArmCD = runtime.time();
            }
            
            if(gamepad1.right_bumper && runtime.time() - slowModeCD >= 0.3){
                slowMode = !slowMode;
                slowModeCD = runtime.time();
            }

            if(gamepad1.a && runtime.time()-clawCD >= 0.2){
                if(clawState <= 4){
                    clawState++;
                } else {
                    clawState = 1;
                }
                
                clawCD = runtime.time();
            }
            
            if(clawState == 1){ //intake
                intakeServo.setPower(-1.0);
            } else if(clawState == 2){ //holdone
                intakeServo.setPower(0.0);
            } else if(clawState == 3){ //outtake
                intakeServo.setPower(1.0);
            } else { //holdtwo
                intakeServo.setPower(0.0);
            }
            
            // if(gamepad1.x){
            //     pivotIntakeMotorPower = 0.4;
            // }
            // if(gamepad1.y){
            //     pivotIntakeMotorPower = -0.55;
            // } 
            // if (!gamepad1.x && !gamepad1.y){
            //     pivotIntakeMotorPower = 0.2;
            // }

            checkAndSetSlideState(); // So this is jus thte switch states code but turned neater

            // Send calculated power to wheels
            if(slowMode){
                leftFrontDrive.setPower(leftFrontPower / 2);
                rightFrontDrive.setPower(rightFrontPower / 2);
                leftBackDrive.setPower(leftBackPower / 2);
                rightBackDrive.setPower(rightBackPower / 2);
                // intakePivotMotor.setPower(pivotIntakeMotorPower / 2);
            } else {
                leftFrontDrive.setPower(leftFrontPower);
                rightFrontDrive.setPower(rightFrontPower);
                leftBackDrive.setPower(leftBackPower);
                rightBackDrive.setPower(rightBackPower);
                // intakePivotMotor.setPower(pivotIntakeMotorPower);
            }
    
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Motor Position:", motorPosition);
            telemetry.addData("IntakePivotEncoders:", pivotPosition);
            telemetry.update();
        }

    }
    
    public void checkAndSetSlideState()
    {
        if(slideArmState == LinearSlideStates2.SlideUp){
            slideArm.setPower(-1.0);
                
            if(motorPosition <= -3010.0){ //swapped?
                slideArmState = slideArmState.nextState();
            }
        } else if(slideArmState == LinearSlideStates2.HoldingOne){
            slideArm.setPower(-0.15);
        } else if(slideArmState == LinearSlideStates2.SlideDown){
            slideArm.setPower(1.0);
            
            if(motorPosition >= -30.0){
                slideArmState = slideArmState.nextState();
            }
        } else if(slideArmState == LinearSlideStates2.HoldingTwo){
            slideArm.setPower(-0.15);
        }
    }
}
