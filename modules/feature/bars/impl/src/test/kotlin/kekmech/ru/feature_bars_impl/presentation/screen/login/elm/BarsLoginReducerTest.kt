package kekmech.ru.feature_bars_impl.presentation.screen.login.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEffect as Effect
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.login.elm.BarsLoginEvent.Ui

class BarsLoginReducerTest : BehaviorSpec({
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
            Then("CodeState is SendingCode with provider TOTP") {
                state.twoFactorCodeState.codeState shouldBe CodeState.SendingCode(CodeProvider.TOTP)
            }
            Then("RequestTwoFactorCode(TOTP) command is emitted on init to initialize challenge") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.TOTP)))
            }
        }

        When("Internal.LoginWithPasswordSuccess with TwoFactorRequired (MAX)") {
            val (state, _, commands) = reducer.reduce(
                Internal.LoginWithPasswordSuccess(
                    LoginStatus.TwoFactorRequired(
                        defaultProvider = CodeProvider.MAX,
                        providers = listOf(CodeProvider.MAX, CodeProvider.TOTP),
                        serverMessage = null,
                    )
                ),
                initialState,
            )

            Then("Stage is TWO_FACTOR_CODE") {
                state.stage shouldBe BarsLoginStage.TWO_FACTOR_CODE
            }
            Then("CodeState is SendingCode with provider MAX") {
                state.twoFactorCodeState.codeState shouldBe CodeState.SendingCode(CodeProvider.MAX)
            }
            Then("Commands contain RequestTwoFactorCode(MAX)") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.MAX)))
            }
        }

        When("Internal.LoginWithPasswordSuccess with TwoFactorRequired (TG)") {
            val (state, _, commands) = reducer.reduce(
                Internal.LoginWithPasswordSuccess(
                    LoginStatus.TwoFactorRequired(
                        defaultProvider = CodeProvider.TG,
                        providers = listOf(CodeProvider.TG, CodeProvider.MAX),
                        serverMessage = null,
                    )
                ),
                initialState,
            )

            Then("Stage is TWO_FACTOR_CODE") {
                state.stage shouldBe BarsLoginStage.TWO_FACTOR_CODE
            }
            Then("CodeState is SendingCode with provider TG") {
                state.twoFactorCodeState.codeState shouldBe CodeState.SendingCode(CodeProvider.TG)
            }
            Then("Commands contain RequestTwoFactorCode(TG)") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.TG)))
            }
        }

        When("Internal.LoginWithPasswordSuccess with TwoFactorRequired (EMAIL)") {
            val (state, _, commands) = reducer.reduce(
                Internal.LoginWithPasswordSuccess(
                    LoginStatus.TwoFactorRequired(
                        defaultProvider = CodeProvider.EMAIL,
                        providers = listOf(CodeProvider.EMAIL, CodeProvider.TOTP),
                        serverMessage = null,
                    )
                ),
                initialState,
            )

            Then("Stage is TWO_FACTOR_CODE") {
                state.stage shouldBe BarsLoginStage.TWO_FACTOR_CODE
            }
            Then("CodeState is SendingCode with provider EMAIL") {
                state.twoFactorCodeState.codeState shouldBe CodeState.SendingCode(CodeProvider.EMAIL)
            }
            Then("Commands contain RequestTwoFactorCode(EMAIL)") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.EMAIL)))
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

        When("Internal.RequestTwoFactorCodeSuccess for MAX") {
            val (state, _, commands) = reducer.reduce(
                Internal.RequestTwoFactorCodeSuccess(
                    provider = CodeProvider.MAX,
                    debounceSec = 30,
                    serverMessage = "Сообщение отправлено в 'MAX'",
                ),
                initialState,
            )

            Then("Debounce seconds is 30 for MAX") {
                val codeSent = state.twoFactorCodeState.codeState as CodeState.CodeSent
                codeSent.resendDebounceSec shouldBe 30
                codeSent.serverMessage shouldBe "Сообщение отправлено в 'MAX'"
            }
            Then("SubscribeTwoFactorCodeTimer command is emitted") {
                commands.shouldContainExactly(listOf(Command.SubscribeTwoFactorCodeTimer))
            }
        }

        When("Internal.RequestTwoFactorCodeSuccess for TG") {
            val (state, _, commands) = reducer.reduce(
                Internal.RequestTwoFactorCodeSuccess(
                    provider = CodeProvider.TG,
                    debounceSec = 30,
                    serverMessage = null,
                ),
                initialState,
            )

            Then("Debounce seconds is 30 for TG") {
                val codeSent = state.twoFactorCodeState.codeState as CodeState.CodeSent
                codeSent.resendDebounceSec shouldBe 30
            }
            Then("SubscribeTwoFactorCodeTimer command is emitted") {
                commands.shouldContainExactly(listOf(Command.SubscribeTwoFactorCodeTimer))
            }
        }

        When("Internal.RequestTwoFactorCodeSuccess for EMAIL") {
            val (state, _, commands) = reducer.reduce(
                Internal.RequestTwoFactorCodeSuccess(
                    provider = CodeProvider.EMAIL,
                    debounceSec = 30,
                    serverMessage = null,
                ),
                initialState,
            )

            Then("Debounce seconds is 30 for EMAIL") {
                val codeSent = state.twoFactorCodeState.codeState as CodeState.CodeSent
                codeSent.resendDebounceSec shouldBe 30
            }
            Then("SubscribeTwoFactorCodeTimer command is emitted") {
                commands.shouldContainExactly(listOf(Command.SubscribeTwoFactorCodeTimer))
            }
        }

        When("Ui.Click.Resend switching to TOTP") {
            val (state, _, commands) = reducer.reduce(
                Ui.Click.Resend(CodeProvider.TOTP),
                initialState,
            )

            Then("CodeState becomes SendingCode(TOTP)") {
                state.twoFactorCodeState.codeState shouldBe CodeState.SendingCode(CodeProvider.TOTP)
                state.twoFactorCodeState.isLoading shouldBe true
            }
            Then("RequestTwoFactorCode(TOTP) command is emitted") {
                commands.shouldContainExactly(listOf(Command.RequestTwoFactorCode(CodeProvider.TOTP)))
            }
        }
    }
})
