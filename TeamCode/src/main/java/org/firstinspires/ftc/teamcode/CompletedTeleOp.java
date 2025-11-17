package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Combined Mecanum + Intake", group = "Linear OpMode")
public class CombinedTeleOp extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // ----------- Drivetrain Motors -----------
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    // ----------- Intake System -----------
    private DcMotor Intake = null;
    private DcMotor Outtake = null;
    private Servo l_feeder = null;
    private Servo r_feeder = null;

    @Override
    public void runOpMode() {

        // =======================
        // DRIVETRAIN INITIALIZATION
        // =======================
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight  = hardwareMap.get(DcMotor.class, "back_right_drive");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // =======================
        // INTAKE SYSTEM INITIALIZATION
        // =======================
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Outtake = hardwareMap.get(DcMotor.class, "Outtake");
        l_feeder = hardwareMap.get(Servo.class, "left_feeder");
        r_feeder = hardwareMap.get(Servo.class, "right_feeder");

        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Intake.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // =======================
        // MAIN LOOP
        // =======================
        while (opModeIsActive()) {

            // ---------------------
            // MECANUM DRIVETRAIN
            // ---------------------
            double y  = -gamepad1.left_stick_y;
            double x  = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double frontLeftPower  = y - x + rx;
            double backLeftPower   = y + x - rx;
            double frontRightPower = y - x - rx;
            double backRightPower  = y + x + rx;

            double max = Math.max(1.0, Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower),
                            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))));

            frontLeftPower  /= max;
            backLeftPower   /= max;
            frontRightPower /= max;
            backRightPower  /= max;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // ---------------------
            // INTAKE + OUTTAKE
            // ---------------------
            Intake.setPower(gamepad1.right_trigger); // RT = Intake
            Outtake.setPower(gamepad1.left_trigger); // LT = Outtake

            // ---------------------
            // FEEDER SERVOS
            // ---------------------
            if (gamepad1.x) {          // feed forward
                l_feeder.setPosition(1);
                r_feeder.setPosition(0);
            } else if (gamepad1.y) {   // reverse
                l_feeder.setPosition(0);
                r_feeder.setPosition(1);
            }

            // ---------------------
            // TELEMETRY
            // ---------------------
            telemetry.addData("Status", "Run Time: " + runtime);
            telemetry.addData("Drive", "FL %.2f | FR %.2f | BL %.2f | BR %.2f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
