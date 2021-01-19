package Interfaces;

import Engine.Datacontainers.Vector3;

public interface Collider{
	public boolean isColliding(Vector3 vec3, float size);
}
