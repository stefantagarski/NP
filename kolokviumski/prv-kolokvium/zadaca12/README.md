Дадена е класата Applicant (кандидат за евалуација) со име, кредитен резултат (credit score), работен стаж (experience) и дали има криминално досие (criminal record). Исто така, даден е интерфејс за евалуација на кандидати Evaluator со еден метод boolean evaluate(Applicant applicant) и енумерација со следните видови на евалуатори:

Кандидатот ја поминува евалуацијата (методот враќа true) ако:

NO_CRIMINAL_RECORD - нема криминално досие
MORE_EXPERIENCE - има барем 10 години работен стаж
MORE_CREDIT_SCORE - има кредитен резултат од минимум 500
NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE - нема криминално досие и има барем 10 години работен стаж
MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE - има барем 10 години работен стаж и има кредитен резултат од минимум 500
NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE - нема криминално досие и има кредитен резултат од минимум 500.
Ваша задача е да го имплементирате методот build(Evaluator.TYPE type) во класата EvaluatorBuilder, кој ќе враќа објект од соодветна имплементација на интерфејсот Evaluator за соодветниот тип на евалуација. Ако типот на евалуација не е некој од наведените, методот треба да фрли исклучок од тип InvalidEvaluation.

За добро дизајнирано решение (ќе биде објавено по завршувањето на испитот) ќе се добиваат бонус 10 поени.