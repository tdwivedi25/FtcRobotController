package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic: Tank Drive", group="Linear OpMode")
public class BasicTankDrive extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    @Override
    public void runOpMode() {

        // Initialize the motors
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // Reverse one side to make forward consistent
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // Tank drive: left stick controls left motor, right stick controls right motor
            double leftPower  = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            // Send power to motors
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // Telemetry for debugging
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Left/Right Power", "%4.2f, %4.2f", leftPower, rightPower);
            telemetry.update();
        }
    }
}
