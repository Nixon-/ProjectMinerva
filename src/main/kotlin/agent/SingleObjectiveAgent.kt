package agent

import environment.*

abstract class SingleObjectiveAgent(name: String, env: Environment, configuration: AgentConfiguration) :
        Agent(name, env, configuration) {

    val objective = configuration.objectives.getAllObjectiveTypes().associateBy({it},
            { ObjectiveEvaluator("singleObjective", configuration.objectives.getType(it.type))})

    var reward = configuration.objectives.getAllObjectiveTypes().associateBy({it}, {0.0}) as MutableMap

    override fun evaluateResponse(model: EnvironmentModel, action: Action.ActionType, response: Feedback) {
        response.rewards.forEach { this.reward[it.key] = this.reward[it.key]!! + response.rewards[it.key]!! }
        response.rewards.forEach { pair ->
            this.objective[pair.key]?.receiveEpisode(Episode(model, action, response.rewards[pair.key]?: 0.0)) }
    }

    override fun learn() {
        this.policy.adapt(objective)
    }

    override fun getTotalReward() : Double {
        return this.reward.values.sum()
    }

}