package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Basic: Mecanum Drive", group = "Linear OpMode")
public class BasicMecanumDrive extends LinearOpMode {

    // A timer object that tracks how long the OpMode has been running
    private ElapsedTime runtime = new ElapsedTime();

    // Declare 4 motors
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    @Override
    public void runOpMode() {

        // Map the motor variables to the names used in the configuration on the Control Hub
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight  = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Reverse the direction of the left side so that forward on the stick means forward on the robot
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Show initialization on Driver Station
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the driver to press PLAY on the Driver Station
        waitForStart();
        runtime.reset();

        // Run until STOP is pressed
        while (opModeIsActive()) {

            // Read the joystick values
            double y = -gamepad1.left_stick_y;  // Forward/backward
            double x =  gamepad1.left_stick_x;  // Left/right strafing
            double rx = gamepad1.right_stick_x; // Rotation (turning)

            // Calculate motor powers using the mecanum drive equations
            double frontLeftPower  = y + x + rx;
            double backLeftPower   = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower  = y + x - rx;

            // Normalize the powers so no motor exceeds 1.0
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower  /= max;
                backLeftPower   /= max;
                frontRightPower /= max;
                backRightPower  /= max;
            }

            // Apply power to each motor
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // Send telemetry data to the Driver Station
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front Motors", "left (%.2f), right (%.2f)", frontLeftPower, frontRightPower);
            telemetry.addData("Back Motors", "left (%.2f), right (%.2f)", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
