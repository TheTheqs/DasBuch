import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AuthorDTO } from "../type/AuthorDTO";
import AuthorService from "../services/AuthorService";
import { handleApiError } from "../utils/handleApiError";
import AuthorForm from "../components/AuthorForm";
import { useTranslation } from "react-i18next";

function EditAuthorPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const [author, setAuthor] = useState<AuthorDTO | null>(null);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchBook = async () => {
      try {
        const data = await AuthorService.getAuthorById(Number(id));
        setAuthor(data);
      } catch (err) {
        setError(handleApiError(err));
      } finally {
        setLoading(false);
      }
    };

    fetchBook();
  }, [id]);

  const handleUpdate = async (formData: { name: string }) => {
    setSuccess("");
    setError("");

    try {
      await AuthorService.updateAuthor(Number(id), {
        name: formData.name,
      });
      setSuccess(t("editAuthor.success"));
      navigate(`/author/${id}`);
    } catch (err) {
      setError(handleApiError(err));
    }
  };

  if (loading) return <p className="text-center mt-4">{t("editAuthor.loading")}</p>;

  if (error || !author)
    return (
      <p className="text-danger text-center mt-4">
        {error || t("editAuthor.notFound")}
      </p>
    );

  const initialData = {
    name: author.name,
  };

  return (
    <AuthorForm
      initialData={initialData}
      onSubmit={handleUpdate}
      submitLabel={t("form.update")}
      success={success}
      error={error}
    />
  );
}

export default EditAuthorPage;
