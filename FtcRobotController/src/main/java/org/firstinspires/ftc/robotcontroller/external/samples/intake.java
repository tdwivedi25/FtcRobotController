package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "Intake test")
public class intake extends LinearOpMode {

    private DcMotor Intake = null;
    private DcMotor Outtake = null;
    private Servo r_feeder = null;
    private Servo l_feeder = null;
    @Override
    public void runOpMode(){
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Outtake = hardwareMap.get(DcMotor.class, "Outtake");
        l_feeder = hardwareMap.get(Servo.class, "left_feeder");
        r_feeder = hardwareMap.get(Servo.class, "right_feeder");
        Intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Outtake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Intake.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();

        while(opModeIsActive()){
            Intake.setPower(gamepad1.right_trigger);
            Outtake.setPower(gamepad1.left_trigger);
            if(gamepad1.x){
                l_feeder.setPosition(1);
                r_feeder.setPosition(0);
            }
            else if(gamepad1.y){
                l_feeder.setPosition(0);
                r_feeder.setPosition(1);
            }
        }
    }
}
