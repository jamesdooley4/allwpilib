/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.trajectory.constraint;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;

/**
 * Enforces a particular constraint only within a rectangular region.
 */
public class RectangularRegionConstraint implements TrajectoryConstraint {
  private final Translation2d m_bottomLeftPoint;
  private final Translation2d m_topRightPoint;
  private final TrajectoryConstraint m_constraint;

  /**
   * Constructs a new RectangularRegionConstraint.
   *
   * @param bottomLeftPoint The bottom left point of the rectangular region in which to
   *                        enforce the constraint.
   * @param topRightPoint   The top right point of the rectangular region in which to enforce
   *                        the constraint.
   * @param constraint      The constraint to enforce when the robot is within the region.
   */
  public RectangularRegionConstraint(Translation2d bottomLeftPoint, Translation2d topRightPoint,
                                     TrajectoryConstraint constraint) {
    m_bottomLeftPoint = bottomLeftPoint;
    m_topRightPoint = topRightPoint;
    m_constraint = constraint;
  }

  @Override
  public double getMaxVelocityMetersPerSecond(Pose2d poseMeters, double curvatureRadPerMeter,
                                              double velocityMetersPerSecond) {
    if (isPoseInRegion(poseMeters)) {
      return m_constraint.getMaxVelocityMetersPerSecond(poseMeters, curvatureRadPerMeter,
          velocityMetersPerSecond);
    } else {
      return Double.POSITIVE_INFINITY;
    }
  }

  @Override
  public MinMax getMinMaxAccelerationMetersPerSecondSq(Pose2d poseMeters,
                                                       double curvatureRadPerMeter,
                                                       double velocityMetersPerSecond) {
    if (isPoseInRegion(poseMeters)) {
      return m_constraint.getMinMaxAccelerationMetersPerSecondSq(poseMeters,
          curvatureRadPerMeter, velocityMetersPerSecond);
    } else {
      return new MinMax();
    }
  }

  /**
   * Returns whether the specified robot pose is within the region that the constraint
   * is enforced in.
   *
   * @param robotPose The robot pose.
   * @return Whether the robot pose is within the constraint region.
   */
  public boolean isPoseInRegion(Pose2d robotPose) {
    return robotPose.getTranslation().getX() >= m_bottomLeftPoint.getX()
        && robotPose.getTranslation().getX() <= m_topRightPoint.getX()
        && robotPose.getTranslation().getY() >= m_bottomLeftPoint.getY()
        && robotPose.getTranslation().getY() <= m_topRightPoint.getY();
  }
}
