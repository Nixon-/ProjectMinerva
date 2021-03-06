package agent.blocks


class LearningProvider {

    fun getQLearning(alpha: Double, gamma: Double) : (Double, Double, Double) -> Double =
        {reward, current, future ->  current + alpha * (reward + gamma * future - current)}

}