package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Basic: Mecanum Drive", group = "Linear OpMode")
public class BasicMecanumDrive extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Declare 4 motors
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    @Override
    public void runOpMode() {

        // Initialize motors
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight  = hardwareMap.get(DcMotor.class, "back_right_drive");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Set motor directions so forward is forward
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // Joystick controls:
            double y  = -gamepad1.left_stick_y;  // Forward/backward (invert for natural control)
            double x  = gamepad1.left_stick_x;   // Strafe left/right
            double rx = gamepad1.right_stick_x;  // Rotation

            // Mecanum drive calculations
            double frontLeftPower  = y - x + rx;
            double backLeftPower   = y + x - rx;
            double frontRightPower = y - x - rx;
            double backRightPower  = y + x + rx;

            // Normalize powers
            double max = Math.max(1.0, Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(frontRightPower),
                            Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)))));

            frontLeftPower  /= max;
            backLeftPower   /= max;
            frontRightPower /= max;
            backRightPower  /= max;

            // Apply power
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Motors", "left (%.2f), right (%.2f)", frontLeftPower, frontRightPower);
            telemetry.addData("Back Motors", "left (%.2f), right (%.2f)", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
