package environment

interface EnvironmentModel {
    override fun hashCode() : Int
    override operator fun equals(other: Any?) : Boolean
    fun deepCopy(): EnvironmentModel;
}