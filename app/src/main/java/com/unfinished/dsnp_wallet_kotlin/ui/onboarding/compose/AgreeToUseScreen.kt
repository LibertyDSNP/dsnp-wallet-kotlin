package com.unfinished.dsnp_wallet_kotlin.ui.onboarding.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unfinished.dsnp_wallet_kotlin.R
import com.unfinished.dsnp_wallet_kotlin.util.Tag
import com.unfinished.uikit.MainColors
import com.unfinished.uikit.MainTheme
import com.unfinished.uikit.MainTypography
import com.unfinished.uikit.components.Bullet
import com.unfinished.uikit.components.PrimaryButton
import com.unfinished.uikit.components.PullDown
import com.unfinished.uikit.exts.tag
import my.nanihadesuka.compose.ColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionMode

@Composable
fun AgreeToUseScreen(
    agreeClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8F)
            .background(MainColors.bottomSheetBackground)
            .padding(horizontal = 36.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Column(
            modifier = Modifier.weight(1F)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PullDown(
                    modifier = Modifier.tag(Tag.AgreeToUseScreen.pullDown)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = stringResource(R.string.agree_to_terms_of_use),
                style = MainTypography.bodyLarge,
                color = MainColors.onBottomSheetBackground,
                modifier = Modifier.tag(Tag.AgreeToUseScreen.title)
            )

            Spacer(modifier = Modifier.size(8.dp))
            ColumnScrollbar(
                state = scrollState,
                selectionMode = ScrollbarSelectionMode.Full,
                padding = 0.dp,
                thumbColor = MainColors.scrollbar,
                thumbSelectedColor = MainColors.scrollbar
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(state = scrollState)
                ) {

                    /**
                     * TODO: This isn't final. Waiting on legal on what we are going to do with
                     * display legal text for this screen. This is a placeholder.
                     */
                    Text(
                        text = """
                            Please read these Amplica Access Terms of Service (these “Terms”) and our Privacy Notice (“Privacy Notice”) carefully because they govern your use of Amplica Access, a software application enabling users to authenticate to, set permissions and interact with Supported Apps and Supported Networks (each as are defined below) as more fully described in these Terms, accessible through the Supported Apps' applicable websites and/or mobile applications via application program interfaces and browser plugins and our websites and/or mobile applications.

                            To make these Terms easier to read:

                            Amplica Labs LLC is referred to as “Amplica”, “we,” “us,” or “our.”
                            Amplica Access, as well as the associated API and browser plugins that connect to Supported
                            Apps, websites, mobile applications and any other features, tools, materials, and other services offered from time to time by Amplica are referred to collectively as the “Services.”
                            “You” and “your” refer to you, as a user of the Services. A “user” is someone who accesses or in any way uses the Services.
                            IMPORTANT NOTICE REGARDING ARBITRATION: WHEN YOU AGREE TO THESE TERMS YOU ARE AGREEING (WITH LIMITED EXCEPTION) TO RESOLVE ANY DISPUTE BETWEEN YOU AND AMPLICA THROUGH BINDING, INDIVIDUAL ARBITRATION RATHER THAN IN COURT. PLEASE REVIEW CAREFULLY SECTION 18 BELOW FOR DETAILS REGARDING ARBITRATION. HOWEVER, IF YOU ARE A RESIDENT OF A JURISDICTION WHERE APPLICABLE LAW PROHIBITS ARBITRATION OF DISPUTES, THE AGREEMENT TO ARBITRATE IN SECTION 18 WILL NOT APPLY TO YOU BUT THE PROVISIONS OF SECTION 18 (GOVERNING LAW AND FORUM CHOICE) WILL APPLY INSTEAD.

                            1. Agreement to Terms and Privacy Notice
                            By using or accessing the Services, you agree to be bound by these Terms as well as our Privacy Notice which provides information on how we collect, use and share your information. If you don't agree to be bound by these Terms or our Privacy Notice, do not use or access the Services. If you are accessing or using the Services on behalf of a company (such as your employer) or other legal entity, you represent and warrant that you have the authority to bind that entity to these Terms. In that case, “you” and “your” will also refer to that entity.

                            2. Changes to these Terms or the Services
                            We may update these Terms and the Services, from time to time, at our sole discretion. If we do, we will let you know by providing the updated Terms through the Services. It's important that you review these Terms whenever we update them or you use or access the Services. If you continue to use or access the Services after we have provided updated Terms it means that you accept and agree to the changes. If you don't agree to be bound by the changes, you may not use or access the Services anymore. Because the Services are evolving over time we may change or discontinue all or any part of the Services, at any time and without notice, at our sole discretion.

                            3. Eligibility
                            You may use the Services only if you are 16 years or older, are capable of forming a binding contract with Amplica, and are not barred from using the Services under applicable law. If you are under the age of majority in your jurisdiction, you must receive your parent's or guardian's permission to use the Services, and “you” and “your” as used in these Terms also means your parent or guardian.

                            By using or accessing the Services, you represent to us that you are: (1) not subject to sanctions, embargoes, export restrictions or otherwise designated on any list of prohibited or restricted parties, including but not limited to the lists maintained by the United Nations Security Council, the U.S. Government (e.g., the Specially Designated Nationals List and Foreign Sanctions Evaders List of the U.S. Department of Treasury and the Entity List of the U.S. Department of Commerce), the European Union or its Member States, or other applicable government authority; (2) not located in (or a citizen of) any jurisdiction to which the United States has embargoed goods or has otherwise applied any sanctions; and (3) are otherwise authorized access to the Services under applicable law.

                            4. Acknowledgment of Development Status of the Services
                            You acknowledge and agree that: (a) the Services are still under development and have not been commercially released by Amplica or otherwise disclosed to the public; (b) the Services are not in final form and may not operate properly or be fully functional; (c) the Services may contain errors, design flaws or other problems; (d) it may not be possible to make the Services fully functional; (e) the information obtained using the Services may not be accurate; (f) use of the Services may result in unexpected results, loss of data or communications, project delays or other unpredictable damage or loss, and it is your responsibility to back up and restore any data that you store in or transmit through the Services; (g) Amplica is under no obligation to release a commercial version of the Services; and (h) Amplica has the right unilaterally to abandon development of the Services, at any time and without any obligation or liability to you.

                            5. Functionality
                            The Services enable you to interact with the Frequency chain, a layer 1 blockchain integrated into the Polkadot platform, and/or other supported networks (together, the “Supported Networks”). Through such interaction, you will be able to: (a) establish a message source account that allows you to set certain permissions and access functions (the “MSA”); (b) store the digital representation of your online relationships in the form of a social graph (the “Social Graph”); (c) establish a self-selected, human readable identifier used to identify the user's MSA (“Handle”) and (d) interact with other applications, including such applications' services, content, features, products, or other functionalities, that Amplica may, in our sole discretion, elect to support through integration with our API, browser plugins, websites and/or mobile applications (together, the “Supported Apps”). The Social Graph, Handle and other information on the Supported Networks are tied to your MSA.

                            AMPLICA IS NOT RESPONSIBLE FOR, AND CANNOT UNILATERALLY CREATE, MODIFY OR DELETE YOUR MSA, SOCIAL GRAPH, HANDLE AND OTHER INFORMATION ON THE SUPPORTED NETWORKS.

                            THE SERVICES ARE NOT DESIGNED TO, AND YOU MAY NOT USE THE SERVICES TO, RECEIVE, HOLD, TRANSMIT, OR TRANSFER CRYPTOCURRENCY OR OTHER CURRENCIES OR MONETARY OR FINANCIAL ASSETS. THE SERVICES AS OFFERED BY AMPLICA ARE ONLY IN CONNECTION WITH NON-FINANCIAL SOCIAL INTERACTION WITHIN A SOCIAL NETWORK.

                            6. Account Password and Security
                            6.1. Account and Access Credentials. To access and use our Services, you will need to set up an account. When setting up an account within the Services, a cryptographic private and public key pair is generated, which evidence your ownership of your MSA and enables you to engage in activities involving the Supported Networks and Supported Apps. The public key is visible to all participants in the Supported Networks. The private key must be used to grant or revoke permission for a Supported App to access your MSA and other associated information as represented by the corresponding public key. Amplica encrypts your private key in a process designed to help ensure its security and confidentiality. Amplica's proprietary software enables you to initiate an approval or rejection of access to your MSA and other associated information using your phone number, email address, or other access credential as further described below. Amplica does not store your private key in its database. Amplica utilizes cloud based key management services and hardware security modules to encrypt your private key in a process designed to help ensure its security and confidentiality. Amplica cannot access raw private key information for you, and we will not unilaterally initiate any such approvals or rejections in connection with, or access, your MSA, Social Graph, Handle and other associated information. We are not responsible for any activities that you engage in when using the Services, you are solely responsible for any and all approvals or rejections of access to your MSA, Social Graph, Handle and other associated information that are initiated through the Services and we make no, and hereby disclaim all, representations, warranties, claims and assurances as to any such initiations of approvals or rejections. You agree to immediately notify Amplica of any unauthorized use of your account or breach of security. You further represent and agree that Amplica shall not be liable for any loss or damage arising from your failure to comply with this section.
                            6.2. Accuracy of Account Information. It's important that you provide us with accurate, complete and current information for your account and you agree to keep this information up- to-date. If you don't, we might have to suspend or terminate your account. You're responsible for all activities that occur under your account. We may take actions we deem reasonably necessary to prevent fraud and abuse, including placing restrictions on user accounts. If you lose access to the phone number, email address, or other access credential used for your account, you may not be able to recover access to your account.
                            6.3. Compliance. In the event of any prohibited use of the Services or our systems, including using the Services to receive, hold, transmit, or transfer cryptocurrency or other currencies or monetary or financial assets, you acknowledge and agree that Amplica, in its sole discretion, may determine what, if any, actions Amplica will take, including suspending or terminating your account, exporting any information that we hold on your behalf to you, or giving you the option to receive such export, subject to applicable laws.
                            7. Feedback
                            We welcome feedback, comments and suggestions for improvements or additions to the Services (“Feedback”). You can submit Feedback by emailing us at feedback@amplicaaccess.com or through functionality that we may include in the Services. You grant us a non-exclusive, transferable, worldwide, perpetual, irrevocable, fully-paid, royalty-free license, with the right to sublicense through multiple tiers, under any and all intellectual property rights that you own or control to use, copy, modify, create derivative works based upon and otherwise exploit the Feedback for any purpose, including, but not limited to, the improvement of the Services and our other products, services and technologies.

                            8. Social Graph
                            8.1. Our Services enable you to interact with the Supported Networks to store your Social Graph. The Social Graph may include data about your relation to other MSAs. Amplica may store some information about your Social Graph. Amplica does not curate any portion of your Social Graph nor claim any ownership rights in any portion of your Social Graph and nothing in these Terms will be deemed to restrict any rights that you may have to your Social Graph.
                            8.2. Permissions to Your Social Graph. By making any Social Graph available through the Services you hereby grant to Amplica a non-exclusive, transferable, worldwide, royalty- free license, with the right to sublicense, to use, copy, modify, create derivative works based upon, distribute, publicly display, and publicly perform your Social Graph solely for the purposes of operating and providing the Services.
                            9. General Prohibitions and Amplica's Enforcement Rights.
                            You agree not to do any of the following:
                            Use the Services to: (i) infringe, misappropriate or violate a third party's patent, copyright, trademark, trade secret, moral rights or other intellectual property rights, or rights of publicity or privacy; or (ii) violate, or encourage any conduct that would violate, any applicable law or regulation or would give rise to civil liability;
                            Use the Services to submit information that: (i) is fraudulent, false, misleading or deceptive; (ii) is defamatory, obscene, pornographic, vulgar or offensive; (iii) promotes discrimination, bigotry, racism, hatred, harassment or harm against any individual or group; (iv) is violent or threatening or promotes violence or actions that are threatening to any person or entity; or (v) promotes illegal or harmful activities or substances;
                            Use, display, mirror or frame the Services or any individual element within the Services, Amplica's name, any Amplica trademark, logo or other proprietary information, or the layout and design of any page or form contained on a page, without Amplica's express written consent; Access, tamper with, or use non-public areas of the Services, Amplica's computer systems, or the technical delivery systems of Amplica's providers;
                            Attempt to probe, scan or test the vulnerability of any Amplica system or network or breach any security or authentication measures;
                            Avoid, bypass, remove, deactivate, impair, descramble or otherwise circumvent any technological measure implemented by Amplica or any of Amplica's providers or any other third party (including another user) to protect the Services;
                            Attempt to access or search the Services or download content from the Services using any engine, software, tool, agent, device or mechanism (including spiders, robots, crawlers, data mining tools or the like) other than the software and/or search agents provided by Amplica or other generally available third-party web browsers;
                            Send any unsolicited or unauthorized advertising, promotional materials, email, junk mail, spam, chain letters or other form of solicitation;
                            Use any meta tags or other hidden text or metadata utilizing a Amplica trademark, logo URL or product name without Amplica's express written consent;
                            Use the Services, or any portion thereof, for any commercial purpose or for the benefit of any third party or in any manner not permitted by these Terms;
                            Forge any TCP/IP packet header or any part of the header information in any email or newsgroup posting, or in any way use the Services to send altered, deceptive or false source- identifying information;
                            Attempt to decipher, decompile, disassemble or reverse engineer any of the software used to provide the Services;
                            Interfere with, or attempt to interfere with, the access of any user, host or network, including, without limitation, sending a virus, overloading, flooding, spamming, or mail-bombing the Services;
                            Collect or store any personally identifiable information from the Services from other users of the Services without their express permission;
                            Impersonate or misrepresent your affiliation with any person or entity; or Encourage or enable any other individual to do any of the foregoing.
                            10. DISCLAIMERS; RISKS
                            10.1. Warranty Disclaimer. YOU EXPRESSLY ACKNOWLEDGE AND AGREE THAT USE OF THE SERVICES (INCLUDING ANY PRIVATE KEY STORAGE SERVICE OFFERED AS PART OF THE SERVICES, WHETHER CLOUD OR HARDWARE-BASED) IS AT YOUR SOLE RISK AND THAT THE ENTIRE RISK AS TO SATISFACTORY QUALITY, PERFORMANCE, ACCURACY AND EFFORT IS WITH YOU. THE SERVICES ARE PROVIDED “AS IS,” WITHOUT WARRANTY OF ANY KIND. WITHOUT LIMITING THE FOREGOING, WE EXPLICITLY DISCLAIM ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT AND NON-INFRINGEMENT, AND ANY WARRANTIES ARISING OUT OF COURSE OF DEALING OR USAGE OF TRADE. WE MAKE NO WARRANTY THAT THE SERVICES WILL MEET YOUR REQUIREMENTS OR BE AVAILABLE ON AN UNINTERRUPTED, SECURE, OR ERROR-FREE BASIS. WE MAKE NO WARRANTY REGARDING THE QUALITY, ACCURACY, TIMELINESS, TRUTHFULNESS, COMPLETENESS OR RELIABILITY OF ANY INFORMATION OR CONTENT ON THE SERVICES.
                            10.2. Sophistication and Risk of Cryptographic Systems. By using or accessing the Services, you represent that you understand the inherent risks associated with cryptographic systems; and warrant that you have an understanding of the usage and intricacies of cryptographic tokens, decentralized networks, and blockchain-based software systems.
                            10.3. Risk of Regulatory Actions in One or More Jurisdictions. Amplica as well as any other Supported Network and Supported App, could be impacted by one or more regulatory inquiries or regulatory action, which could impede or limit the ability of Amplica to continue to develop, or which could impede or limit your ability to access or use the Services, the Frequency chain, any other Supported Network, MSA, Handle, Social Graph, and/or any Supported App. By using or accessing the Services, you acknowledge these risks and agree that Amplica will not be held liable for any losses or damages associated with these risks including, without limitation, losses associated with your use of the Social Graph and Supported Networks.
                            10.4. Risk of Weaknesses or Exploits in the Field of Cryptography. You acknowledge and understand that cryptography is a progressing field and that advances in cryptanalysis or technical advances such as the development of quantum computers may present risks to cryptocurrencies and the Services, which could result in the theft or loss of your MSA, Handle or Social Graph. To the extent possible, Amplica intends to update the Services to account for any advances in cryptography and to incorporate additional security measures but does not guarantee or otherwise represent full security of the Services. By using or accessing the Services, you acknowledge these risks and agree that Amplica will not be held liable for any losses or damages associated with these risks including, without limitation, losses associated with your use of the Supported Networks.
                            10.5. Other Affiliated Services and Third Party Services.
                            The Services may include, integrate with, or be accessible through, Supported Apps including software and services provided by our affiliates and or by third parties (“Third Party Services”). Supported Apps are made available to you under the terms of the applicable service providers (collectively, “Service Providers”). Please review the applicable terms prior to using or accessing Supported Apps. By using any Supported Apps, you acknowledge that (i) you have read and agree to the terms that apply to such Supported Apps and (ii) you may be exposed to the risks inherent in such Supported Apps. Such risks include, without limitation, delays in or inability to access funds or cryptographic tokens held by such parties or loss of funds of cryptographic tokens. You agree that Amplica is not responsible for any such liability.
                            Service Providers may charge you a fee for use of, or access to, Supported Apps.
                            You agree that Amplica and its affiliates are not in any way associated with the Third Party Services or responsible or liable for the software and services offered by the associated Service Providers. Amplica does not endorse or approve and makes no warranties, representations or undertakings relating to the software, service or content of any Third Party Services.
                            In addition, Amplica disclaims liability for any loss, damage and any other consequence resulting directly or indirectly from or relating to your use or access of Third Party Services or any information that you may provide or any transaction conducted with or through the Third Party Services or the failure of any information, software or services posted or offered by such Service Providers or any error, omission or misrepresentation by such Service Providers or any computer virus arising from or system failure associated with the Third Party Services.
                            In the event of any inconsistency between these Terms and the terms of the Supported Apps, these Terms will prevail with regard to any matter concerning Amplica's provision of the Services to you.
                            11. Indemnity
                            You will indemnify and hold Amplica and its officers, directors, employees and agents, harmless from and against any claims, disputes, demands, liabilities, damages, losses, and costs and expenses, including, without limitation, reasonable legal and accounting fees arising out of or in any way connected with (a) your access to or use of the Services, (b) your MSA, Handle or Social Graph or (c) your violation of these Terms.

                            12. Limitation on Liability
                            12.1. For the purposes of this Section 12, “Amplica”, “we”, or “us” shall include Amplica, its parent company, subsidiaries, affiliates, investors, agents, and successors and assigns.
                            12.2. TO THE MAXIMUM EXTENT PERMITTED BY LAW, NEITHER AMPLICA NOR ITS SERVICE PROVIDERS INVOLVED IN CREATING, PRODUCING, OR DELIVERING THE SERVICES WILL BE LIABLE FOR ANY INCIDENTAL, SPECIAL, EXEMPLARY OR CONSEQUENTIAL DAMAGES, OR DAMAGES FOR LOST PROFITS, LOST REVENUES, LOST SAVINGS, LOST BUSINESS OPPORTUNITY, LOSS OF DATA OR GOODWILL, SERVICE INTERRUPTION, COMPUTER DAMAGE OR SYSTEM FAILURE OR THE COST OF SUBSTITUTE SERVICES OF ANY KIND ARISING OUT OF OR IN CONNECTION WITH THESE TERMS OR FROM THE USE OF OR INABILITY TO USE THE SERVICES, WHETHER BASED ON WARRANTY, CONTRACT, TORT (INCLUDING NEGLIGENCE), PRODUCT LIABILITY OR ANY OTHER LEGAL THEORY, AND WHETHER OR NOT AMPLICA OR ITS SERVICE PROVIDERS HAS BEEN INFORMED OF THE POSSIBILITY OF SUCH DAMAGE, EVEN IF A LIMITED REMEDY SET FORTH HEREIN IS FOUND TO HAVE FAILED OF ITS ESSENTIAL PURPOSE. TO THE MAXIMUM EXTENT PERMITTED BY THE LAW OF THE APPLICABLE JURISDICTION, IN NO EVENT WILL AMPLICA'S TOTAL LIABILITY ARISING OUT OF OR IN CONNECTION WITH THESE TERMS OR FROM THE USE OF OR INABILITY TO USE THE SERVICES EXCEED THE AMOUNTS YOU HAVE PAID OR ARE PAYABLE BY YOU TO AMPLICA FOR USE OF THE SERVICES OR ONE HUNDRED DOLLARS (${'$'}100), IF YOU HAVE NOT HAD ANY PAYMENT OBLIGATIONS TO AMPLICA, AS APPLICABLE.
                            12.3. THE EXCLUSIONS AND LIMITATIONS OF DAMAGES SET FORTH ABOVE ARE FUNDAMENTAL ELEMENTS OF THE AMPLICA'S OFFER TO PROVIDE SERVICES.
                            13. Limited License
                            Subject to your compliance with these Terms, we grant you limited, non-exclusive, revocable permission to make use of the Services (“Access”). This Access shall remain in effect until and unless terminated by you or us. You understand that this Access is personal to you and you promise and agree that you will not attempt to redistribute, sublicense, or otherwise attempt to transfer Access or the Services, except as expressly provided under these Terms.

                            We, and our licensors, retain ownership of all copies of the Services, or any part thereof, even after installation on your personal computers, mobile devices, tablets, wearable devices, speakers and/or other devices.

                            All Amplica trademarks, service marks, trade names, logos, domain names, and any other features of the Amplica brand (“Amplica Brand Features”) are the sole property of Amplica or its licensors. The Terms do not grant you any rights to use any Amplica Brand Features whether for commercial or non- commercial use.

                            You agree to abide by our user guidelines and not to use the Services or any part thereof in any manner not expressly permitted by the Terms. Except for the rights expressly granted to you in the Terms, Amplica grants no right, title, or interest to you in the Services.

                            Third party software (for example, open source software libraries) included in the Services are made available to you under the relevant third party software library's license terms. Notwithstanding anything herein to the contrary, nothing in the Terms entitles you to copy, modify, fork, merge, combine with another program or create a derivative work of the Services.

                            14. Links
                            The Services provide, or third parties may provide, links to other sites, applications or resources. Because Amplica has no control over such sites, applications and resources, you acknowledge and agree that Amplica is not responsible for the availability of such external sites, applications or resources, and does not endorse and is not responsible or liable for any content, advertising, products or other materials on or available from such sites or resources. You further acknowledge and agree that Amplica shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with use of or reliance on any such content, goods or services available on or through any such site or resource.
                            15. Termination and Suspension
                            We may suspend or terminate your access to and use of the Services, including suspending access to or terminating your account, at our sole discretion, at any time and without notice to you. You may cancel your account at any time by sending us an email at support@amplicaaccess.com. Upon any termination, discontinuation or cancellation of the Services or your account, the following Sections will survive: 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 19, and this sentence of Section 15.
                            16. No Third Party Beneficiaries
                            You agree that, except as otherwise expressly provided in these Terms, there shall be no third party beneficiaries to the Terms.
                            17. Governing Law and Forum Choice
                            These Terms and any action related thereto will be governed by the Federal Arbitration Act, federal arbitration law, and the laws of the State of California, without regard to its conflict of laws provisions. Except as otherwise expressly set forth in Section 18 “Dispute Resolution,” the exclusive jurisdiction for all Disputes (defined below) that you and Amplica are not required to arbitrate will be the state and federal courts located in Los Angeles, California, and you and Amplica each waive any objection to jurisdiction and venue in such courts.
                            18. Dispute Resolution
                            18.1. Informal Dispute Resolution. You and Amplica must first attempt to resolve any dispute, claim, or controversy arising out of or relating to these Terms, or the breach, termination, enforcement, interpretation, or validity thereof, or the use of the Services (collectively, “Disputes”) informally. Accordingly, neither you nor Amplica may start a formal arbitration proceeding for at least sixty (60) days after one party notifies the other party of a claim in writing. As part of this informal resolution process, you must deliver written notices via hand or first-class mail to us at Amplica, C/O Corporation Trust Center, 1209 Orange Street, Wilmington, Delaware 19801. Notwithstanding the foregoing, we each retain the right to seek injunctive or other equitable relief from a court to prevent (or enjoin) the infringement or misappropriation of our intellectual property rights at any time.
                            18.2. Mandatory Arbitration of Disputes. We each agree that any Dispute will be resolved solely by binding, individual arbitration and not in a class, representative, or consolidated action or proceeding. You and Amplica agree that the U.S. Federal Arbitration Act governs the interpretation and enforcement of these Terms, and that you and Amplica are each waiving the right to a trial by jury or to participate in a class action. This arbitration provision shall survive termination of these Terms.
                            18.3. Exceptions. As limited exceptions to Section 18.2 above: (i) we both may seek to resolve a Dispute in small claims court if it qualifies; and (ii) we each retain the right to seek injunctive or other equitable relief from a court to prevent (or enjoin) the infringement or misappropriation of our intellectual property rights.
                            18.4. Conducting Arbitration and Arbitration Rules. The arbitration will be conducted by the American Arbitration Association (“AAA”) under its Consumer Arbitration Rules (the “AAA Rules”) then in effect, except as modified by these Terms. The AAA Rules are available at www.adr.org or by calling 1-800-778-7879. A party who wishes to start arbitration must submit a written Demand for Arbitration to AAA and give notice to the other party as specified in the AAA Rules. The AAA provides a form Demand for Arbitration at www.adr.org

                            Any arbitration hearings will take place in the county (or parish) where you live, unless we both agree to a different location. The parties agree that the arbitrator shall have exclusive authority to decide all issues relating to the interpretation, applicability, enforceability and scope of this arbitration agreement.

                            18.5. Arbitration Costs. Payment of all filing, administration and arbitrator fees will be governed by the AAA Rules, and we won't seek to recover the administration and arbitrator fees we are responsible for paying, unless the arbitrator finds your Dispute frivolous. If we prevail in arbitration we'll pay all of our attorneys' fees and costs and won't seek to recover them from you. If you prevail in arbitration you will be entitled to an award of attorneys' fees and expenses to the extent provided under applicable law.
                            18.6. Injunctive and Declaratory Relief. Except as provided in Section 18.3 above, the arbitrator shall determine all issues of liability on the merits of any claim asserted by either party and may award declaratory or injunctive relief only in favor of the individual party seeking relief and only to the extent necessary to provide relief warranted by that party's individual claim. To the extent that you or we prevail on a claim and seek public injunctive relief (that is, injunctive relief that has the primary purpose and effect of prohibiting unlawful acts that threaten future injury to the public), the entitlement to and extent of such relief must be litigated in a civil court of competent jurisdiction and not in arbitration. The parties agree that litigation of any issues of public injunctive relief shall be stayed pending the outcome of the merits of any individual claims in arbitration.
                            18.7. Class Action Waiver. YOU AND AMPLICA AGREE THAT EACH MAY BRING CLAIMS AGAINST THE OTHER ONLY IN YOUR OR ITS INDIVIDUAL CAPACITY, AND NOT AS A PLAINTIFF OR CLASS MEMBER IN ANY PURPORTED CLASS OR REPRESENTATIVE PROCEEDING. Further, if the parties' Dispute is resolved through arbitration, the arbitrator may not consolidate another person's claims with your claims, and may not otherwise preside over any form of a representative or class proceeding. If this specific provision is found to be unenforceable, then the entirety of this Dispute Resolution section shall be null and void.
                            18.8. Severability. With the exception of any of the provisions in Section 18.7 of these Terms (“Class Action Waiver”), if an arbitrator or court of competent jurisdiction decides that any part of these Terms is invalid or unenforceable, the other parts of these Terms will still apply.
                            19. General Terms
                            19.1. Reservation of Rights. Amplica and its licensors exclusively own all right, title and interest in and to the Services, including all associated intellectual property rights. You acknowledge that the Services are protected by copyright, trademark, and other laws of the United States and foreign countries. You agree not to remove, alter or obscure any copyright, trademark, service mark or other proprietary rights notices incorporated in or accompanying the Services.
                            19.2. Entire Agreement. These Terms constitute the entire and exclusive understanding and agreement between Amplica and you regarding the Services, and these Terms supersede and replace all prior oral or written understandings or agreements between Amplica and you regarding the Services. If any provision of these Terms is held invalid or unenforceable by an arbitrator or a court of competent jurisdiction, that provision will be enforced to the maximum extent permissible and the other provisions of these Terms will remain in full force and effect. You may not assign or transfer these Terms, by operation of law or otherwise, without Amplica's prior written consent. Any attempt by you to assign or transfer these Terms, without such consent, will be null. Amplica may freely assign or transfer these Terms without restriction. Subject to the foregoing, these Terms will bind and inure to the benefit of the parties, their successors and permitted assigns.
                            19.3. Notices. Any notices or other communications provided by Amplica under these Terms will be given: (i) via email; or (ii) by posting to the Services. For notices made by email, the date of receipt will be deemed the date on which such notice is transmitted.
                            19.4. Waiver of Rights. Amplica's failure to enforce any right or provision of these Terms will not be considered a waiver of such right or provision. The waiver of any such right or provision will be effective only if in writing and signed by a duly authorized representative of Amplica. Except as expressly set forth in these Terms, the exercise by either party of any of its remedies under these Terms will be without prejudice to its other remedies under these Terms or otherwise.
                            20. Contact Information
                            If you have any questions about these Terms or the Services, please contact Amplica at support@amplicaaccess.com.
                        """.trimIndent(),
                        style = MainTypography.body,
                        color = MainColors.onBottomSheetBackground,
                        modifier = Modifier.tag(Tag.AgreeToUseScreen.body)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(8.dp))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.AgreeToUseScreen.agree),
            text = stringResource(R.string.agree),
            enabled = !scrollState.canScrollForward,
            onClick = agreeClick
        )

        Spacer(modifier = Modifier.size(22.dp))
        Text(
            text = stringResource(R.string.by_clicking_the_agree_button_you_agree),
            style = MainTypography.bodyMedium,
            color = MainColors.onTaskButton,
            modifier = Modifier
                .fillMaxWidth()
                .tag(Tag.AgreeToUseScreen.bottomText),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(22.dp))
    }

    /**
     * This allows us to display the scrollbars when the user opens this view
     */
    LaunchedEffect(
        key1 = Unit,
        block = {
            scrollState.animateScrollTo(1)
        }
    )
}

@Preview
@Composable
fun SampleAgreeToUseScreen() {
    MainTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AgreeToUseScreen(
                agreeClick = {}
            )
        }
    }
}