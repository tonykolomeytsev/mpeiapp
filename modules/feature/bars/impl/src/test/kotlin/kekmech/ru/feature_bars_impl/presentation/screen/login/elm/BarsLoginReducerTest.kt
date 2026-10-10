package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal

internal class BarsLoginReducerTest : BehaviorSpec({
    val reducer = BarsLoginReducer()

    Given("Initial state") {
        val initialState = BarsLoginState()

        When("Internal.LoginWithPasswordSuccess with TwoFactorRequired (TOTP)") {
            val (state, _, commands) = reducer.reduce(
                Internal.LoginWithPasswordSuccess(
                    LoginStatus.TwoFactorRequired(
                        defaultProvider = CodeProvider.TOTP,
                        providers = listOf(CodeProvider.TOTP, CodeProvider.MAX),
                        serverMessage = null,
                    )
                ),
                initialState,
            )

            Then("Stage is TWO_FACTOR_CODE") {
                state.stage shouldBe BarsLoginStage.TWO_FACTOR_CODE
            }
            Then("CodeState is CodeSent with provider TOTP and debounce 0") {
                state.twoFactorCodeState.codeState shouldBe CodeState.CodeSent(
                    provider = CodeProvider.TOTP,
                    resendDebounceSec = 0,
                    serverMessage = null,
                )
            }
            Then("Commands contain RequestTwoFactorCode(TOTP)") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.TOTP)))
            }
        }

        When("Internal.Submit2faCodeSuccess with InvalidCode") {
            val stateWithLoading = initialState.copy(
                twoFactorCodeState = initialState.twoFactorCodeState.copy(isLoading = true),
            )
            val (state, effects, _) = reducer.reduce(
                Internal.Submit2faCodeSuccess(TwoFactorCodeStatus.InvalidCode),
                stateWithLoading,
            )

            Then("isLoading is reset to false") {
                state.twoFactorCodeState.isLoading shouldBe false
            }
            Then("Effect ShowInvalidCodeText is emitted") {
                effects.shouldContainExactly(listOf(Effect.ShowInvalidCodeText))
            }
        }

        When("Internal.RequestTwoFactorCodeSuccess for TOTP") {
            val (state, _, commands) = reducer.reduce(
                Internal.RequestTwoFactorCodeSuccess(
                    provider = CodeProvider.TOTP,
                    debounceSec = 30,
                    serverMessage = null,
                ),
                initialState,
            )

            Then("Debounce seconds is 0 for TOTP") {
                val codeSent = state.twoFactorCodeState.codeState as CodeState.CodeSent
                codeSent.resendDebounceSec shouldBe 0
            }
            Then("No SubscribeTwoFactorCodeTimer command is emitted") {
                commands shouldBe emptyList()
            }
        }
    }
})
