package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="CompletedTeleOp", group="Linear OpMode")
public class CompletedTeleOp extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Drivetrain Motors
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;

    // Outtake + Extra Power Servos
    private DcMotor outtake = null;
    private CRServo leftServo = null;
    private CRServo rightServo = null;

    @Override
    public void runOpMode() {

        // ================
        // DRIVETRAIN SETUP
        // ================
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRight = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeft   = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRight  = hardwareMap.get(DcMotor.class, "back_right_drive");

        // Set motor directions for mecanum
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // ======================
        // OUTTAKE + SERVO SETUP
        // ======================
        outtake = hardwareMap.get(DcMotor.class, "outtake");
        leftServo = hardwareMap.get(CRServo.class, "left_feeder");   // renamed logically
        rightServo = hardwareMap.get(CRServo.class, "right_feeder");

        outtake.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // ====================
            // MECANUM DRIVETRAIN
            // ====================
            double y  = -gamepad1.left_stick_y;   // Forward/back
            double x  = gamepad1.left_stick_x;    // Strafe
            double rx = gamepad1.right_stick_x;   // Rotate

            double frontLeftPower  = y + x + rx;
            double backLeftPower   = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower  = y + x - rx;

            // Normalize
            double max = Math.max(Math.abs(frontLeftPower),
                    Math.max(Math.abs(backLeftPower),
                            Math.max(Math.abs(frontRightPower),
                                    Math.abs(backRightPower))));

            if (max > 1.0) {
                frontLeftPower  /= max;
                backLeftPower   /= max;
                frontRightPower /= max;
                backRightPower  /= max;
            }

            // Apply drivetrain power
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            // ====================
            // OUTTAKE CONTROL
            // ====================
            double outtakePower = gamepad1.right_trigger;   // forward only
            outtake.setPower(outtakePower);

            // ====================
            // SERVO EXTRA POWER (LEFT TRIGGER)
            // ====================
            double servoPower = gamepad1.left_trigger;      // forward only

            leftServo.setPower(servoPower);
            rightServo.setPower(servoPower);

            if (servoPower == 0) {
                leftServo.setPower(0);
                rightServo.setPower(0);
            }

            // ====================
            // TELEMETRY
            // ====================
            telemetry.addData("Run Time", runtime.toString());
            telemetry.addData("Drive", "FL %.2f, FR %.2f, BL %.2f, BR %.2f",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Outtake Power", outtakePower);
            telemetry.addData("Servo Power", servoPower);
            telemetry.update();
        }
    }
}
