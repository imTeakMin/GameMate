import React, { useEffect, useRef } from "react"; // 1. useRef 추가
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../api/axios";

const TossSuccess = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  // 2. 요청 실행 여부를 추적할 ref 생성
  const hasCalledAPI = useRef(false);

  const orderId = searchParams.get("orderId");
  const paymentKey = searchParams.get("paymentKey");
  const amount = searchParams.get("amount");

  useEffect(() => {
    const confirmPayment = async () => {
      // 3. 이미 요청을 보냈다면 함수 종료
      if (hasCalledAPI.current) return;
      hasCalledAPI.current = true;

      try {
        const token = localStorage.getItem("accessToken");
        const response = await api.post(
          "/toss/confirm",
          {
            paymentKey: paymentKey,
            orderId: orderId,
            amount: Number(amount),
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        );

        if (response.status === 200) {
          alert("코인 충전이 완료되었습니다!");
          navigate("/coinHistory");
        }
      } catch (error) {
        console.error("Payment Confirmation Error:", error.response || error);

        // 4. 만약 이미 처리된 결제라는 에러가 오더라도 사용자에겐 성공 페이지를 보여주는 것이 좋습니다.
        alert(
          error.response?.data?.message || "결제 승인 중 오류가 발생했습니다.",
        );
        navigate("/payment");
      }
    };

    if (orderId && paymentKey && amount) {
      confirmPayment();
    }
  }, [orderId, paymentKey, amount, navigate]);

  return (
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      <h2>결제 승인 중입니다...</h2>
      <p>잠시만 기다려주세요.</p>
    </div>
  );
};

export default TossSuccess;
